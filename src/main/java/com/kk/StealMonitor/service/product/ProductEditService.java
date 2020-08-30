package com.kk.StealMonitor.service.product;

import com.google.common.base.Strings;
import com.kk.StealMonitor.dao.product.ProductDao;
import com.kk.StealMonitor.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.*;
/*
* write comment here
* */
@Service
public class ProductEditService {

    private ProductDao productDao;

    //Lists with ids, to update and delete
    private List<UUID> xKomIds;
    private Map<String,List<UUID>> idLists;

    @Autowired
    public ProductEditService(@Qualifier("PostgresProduct") ProductDao productDao) {
        this.productDao = productDao;
        this.idLists = new HashMap<>();
    }

    public int insertProduct(UUID id, Product newTask) {
        if(Strings.isNullOrEmpty(newTask.getName())
        || Strings.isNullOrEmpty(newTask.getSiteName())
        || Strings.isNullOrEmpty(newTask.getSiteLink())
        || Strings.isNullOrEmpty(newTask.getOldPrice())
        || Strings.isNullOrEmpty(newTask.getNewPrice())
        || Strings.isNullOrEmpty(newTask.getRemainingQuantity())
        || Strings.isNullOrEmpty(newTask.getLimitQuantity())
        || Strings.isNullOrEmpty(newTask.getImg())) {
            return 0;
        }
        return productDao.insertProduct(id, newTask);
    }

    public List<UUID> insertListOfProducts(List<Product> products) {
        List<UUID> idList = new ArrayList<>();
        products.forEach(product -> {
            UUID id = product.getId();
            idList.add(id);
            productDao.insertProduct(id, product);
        });
        return idList;
    }
    public int updateProductsRemaining(UUID id, Product product) {
        return productDao.updateProduct(id, product);
    }

    public int updateListOfProducts(List<UUID> idList, List<Product> products) {
        if(idList.size()!=products.size()) return 0;
        for (int i = 0; i < idList.size(); i++)
            updateProductsRemaining(idList.get(i), products.get(i));
        return 1;
    }

    public int deleteTask(UUID id) {
        return productDao.deleteProduct(id);
    }

    //to testing
    public int deleteListOfProducts(List<UUID> idList) {
        idList.forEach(this::deleteTask);
        return 1;
    }

    public List<UUID> getIdList(String key) {
        key = key.toLowerCase();
        return idLists.get(key);
    }

    public int setOrCreateIdList(String key, List<UUID> idList) {
        System.out.println(idList.size());
        key = key.toLowerCase();
        //check if it exist key like this,
        //in case no, create one and put into idLists
        if(idLists.containsKey(key))
            idLists.replace(key, idList);
        else
            idLists.put(key, idList);
        System.out.println(idList.size());
        return 1;
    }
}
