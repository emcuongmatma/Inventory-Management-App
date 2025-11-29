/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package com.mycompany.inventorymanagementapp.GUI;

import BUS.ProductBUS;
import DAO.BrandDAO;
import DAO.CategoryDAO;
import DAO.CustomerDAO;
import DAO.ProductDAO;
import com.mongodb.client.MongoDatabase;
import com.mycompany.inventorymanagementapp.DTO.BrandDTO;
import com.mycompany.inventorymanagementapp.DTO.CategoryDTO;
import com.mycompany.inventorymanagementapp.DTO.CustomerDTO;
import com.mycompany.inventorymanagementapp.DTO.ProductDTO;
import config.MongoDBConnection;
import java.util.List;

/**
 *
 * @author ECMM
 */
public class InventoryManagementApp {

    public static void main(String[] args) {
        //test product
//        ProductDAO productDAO = new ProductDAO();
//        List<ProductDTO> products = productDAO.findByCodeOrName("lenovo");
//        System.out.println(products);
//        ProductBUS.getInstance().addProduct(new ProductDTO("","Lenovo ThinkBook","hehe",10,5000000,"hehe",0f,"huhu","huuh"));

//        System.out.println(  ProductBUS.getInstance().getByCodeOrName("PD001"));
//        ProductBUS.getInstance().decreaseStock("PD002", 10);
//        ProductBUS.getInstance().deleteProduct("PD002");
        //testbrand
//        BrandDAO brandDao = new BrandDAO();
//        brandDao.insert(new BrandDTO("BR002","Asus"));
//        brandDao.update(new BrandDTO("BR001","lenovo"));
//        System.out.println(brandDao.getAll());
//        System.out.println(brandDao.findByCodeOrName("le"));
//        brandDao.delete("BR002");
        //testCategory
//        CategoryDAO categoryDAO = new CategoryDAO();
//        categoryDAO.insert(new CategoryDTO("CT001", "Laptop"));
//        categoryDAO.insert(new CategoryDTO("CT002", "Laptop"));
//        categoryDAO.update(new CategoryDTO("CT001", "RAM"));
//        System.out.println(categoryDAO.getAll());
//        System.out.println(categoryDAO.findByCodeOrName("a"));
//        categoryDAO.delete("CT002");
        //testCustomer
        CustomerDAO customerDAO = new CustomerDAO();
//        customerDAO.insert(new CustomerDTO("Em Cuong Mat Ma","0326468846","Sao hoa"));
        System.out.println(customerDAO.findByPhone("08"));
    }
}
