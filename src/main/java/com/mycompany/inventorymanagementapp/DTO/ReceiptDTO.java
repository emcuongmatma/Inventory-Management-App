/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.inventorymanagementapp.DTO;
import java.util.Date;
import lombok.Data;
/**
 *
 * @author ECMM
 */
@Data
public class ReceiptDTO {
    private Integer _id;
    private String supplierCode;
    private String note;
    private Date receiptDate;
    private ReceiptItemDTO items;
}
