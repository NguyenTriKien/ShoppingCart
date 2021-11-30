package com.shoppingcart.dao.impl;

import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.shoppingcart.dao.ProductDAO;
import com.shoppingcart.entity.Product;
import com.shoppingcart.model.PaginationResult;
import com.shoppingcart.model.ProductInfo;

@Repository
@Transactional
public class ProductDAOImpl implements ProductDAO {
    
	@Autowired
	private SessionFactory sessionFactory;
	
	@Override
	public PaginationResult<ProductInfo> getAllProductInfos(int page, int maxResult, int maxNavigationPage,
			String likeName) {
		// TODO Auto-generated method stub
		Session session = sessionFactory.getCurrentSession();
		//muốn sử dụng NEW " + ProductInfo
		String hql = " SELECT NEW " + ProductInfo.class.getName() + " (PRO.code, PRO.name, PRO.price) FROM Product PRO ";
		if(likeName != null && likeName.length() > 0) {
			hql += " WHERE LOWER(PRO.name) LIKE :LIKENAME ";
		}
		hql += " ORDER BY PRO.createDate DESC ";
		
		Query<ProductInfo> query = session.createQuery(hql);
		List<ProductInfo> productInfos = query.list();
		
		if(likeName != null && likeName.length() > 0) {
			query.setParameter("LIKENAME", "%" + likeName.toLowerCase() + "%");
		}
		return new PaginationResult<ProductInfo>(query, page, maxResult, maxNavigationPage);
	}
	
	@Override
	public Product getProductByCode(String code) {
		Session session = sessionFactory.getCurrentSession();
		String hql = "SELECT PRO FROM Product PRO WHERE PRO.code = :CODE";
		Query<Product> query = session.createQuery(hql);
		query.setParameter("CODE", code);
		Product product = (Product) query.uniqueResult();
		return product;
		
	}
	
	@Override
	public ProductInfo getProductInfoByCode(String code) {
		Product product = getProductByCode(code);
		if(product == null) {
			return null;
		}
		ProductInfo productInfo = new ProductInfo(product.getCode(), product.getName(), product.getPrice());
		return productInfo;
	}

	@Override
	public void saveProductInfo(ProductInfo productInfo) {
		// TODO Auto-generated method stub
		Session session = sessionFactory.getCurrentSession();
		String code = productInfo.getCode();
		Product product = null;
		boolean isNew = false;
		
		if(code != null) {
			product = getProductByCode(code);
		}
		
		if(product == null) {
			isNew = true;
			product = new Product();
			product.setCreateDate(new Date());
		}
		product.setCode(code);
		product.setName(productInfo.getName());
		product.setPrice(productInfo.getPrice());
		
		if(productInfo.getFileData() != null) {
			byte[] image = productInfo.getFileData().getBytes();
			if(image != null && image.length > 0) {
				product.setImage(image);
			}
		}
		if(isNew) {
			session.persist(product);
		}
		
		session.flush();
	}

}
