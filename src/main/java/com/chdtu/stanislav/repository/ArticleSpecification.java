package com.chdtu.stanislav.repository;

import com.chdtu.stanislav.domain.Article;
import com.chdtu.stanislav.domain.Brand;
import com.chdtu.stanislav.domain.Category;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

public class ArticleSpecification {
	
	private ArticleSpecification() {}
	
	@SuppressWarnings("serial")
	public static Specification<Article> filterBy(Integer priceLow, Integer priceHigh,
												  List<String> categories, List<String> brands, String search) {			
		return new Specification<Article>() {
            @Override
            public Predicate toPredicate(Root<Article> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates = new ArrayList<>();                
                query.distinct(true);                

                if (categories!=null && !categories.isEmpty()) {
                	Join<Article, Category> joinSize = root.join("categories");
                	predicates.add(criteriaBuilder.and(joinSize.get("name").in(categories)));
                }   
                if (brands!=null && !brands.isEmpty()) {
                	Join<Article, Brand> joinSize = root.join("brands");
                	predicates.add(criteriaBuilder.and(joinSize.get("name").in(brands)));
                }  
                
                if(search!=null && !search.isEmpty()) {
                    predicates.add(criteriaBuilder.and(criteriaBuilder.like(root.get("title"), "%"+search+"%")));
                }
                if (priceLow!=null && priceLow >= 0) {
                    predicates.add(criteriaBuilder.and(criteriaBuilder.greaterThanOrEqualTo(root.get("price"), priceLow)));
                }
                if (priceHigh!=null && priceHigh >= 0) {
                    predicates.add(criteriaBuilder.and(criteriaBuilder.lessThanOrEqualTo(root.get("price"), priceHigh)));
                }
                return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        };		
	}
}