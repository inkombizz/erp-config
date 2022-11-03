/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.inkombizz.model;

import com.inkombizz.base.BaseModel;
import io.github.millij.poi.ss.model.annotations.SheetColumn;
import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;

/**
 *
 * @author lukassh
 */
@Data
@Entity
@Table(name = "mst_item_jn_current_stock")
public class InventoryInOut extends BaseModel{
    
    @Id
    @Column(name = "Code")
    @SheetColumn("Code")
    private String code = "";

    @Column(name = "WarehouseCode")
    @SheetColumn("WarehouseCode")
    private String warehouseCode = "";

    @Column(name = "ItemCode")
    @SheetColumn("ItemCode")
    private String itemCode = "";

    @Column(name = "RackCode")
    @SheetColumn("RackCode")
    private String rackCode = "";

    @Column(name = "ActualStock")
    @SheetColumn("ActualStock")
    private BigDecimal actualStock = new BigDecimal(0.00);
    
}
