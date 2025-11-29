/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.inventorymanagementapp.DTO;
import java.util.Date;
import java.util.List;
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
public class ReceiptDTO {
    private Integer _id;
    private String supplierCode;
    private String note;
    private Date receiptDate;
    private List<ReceiptItemDTO> items;
}
