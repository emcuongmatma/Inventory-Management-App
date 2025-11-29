/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.inventorymanagementapp.DTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
/**
 *
 * @author ECMM
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SupplierDTO {
    private String supplierCode;
    private String name;
    private String phone;
    private String email;
    private String address;
}
