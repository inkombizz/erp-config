/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.inkombizz.dao;

import java.util.Map;
import java.util.List;
import java.util.HashMap;
import org.json.JSONArray;
import org.json.JSONObject;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import com.inkombizz.base.BaseQuery;
import com.inkombizz.model.InventoryInOut;
import com.inkombizz.base.PaginatedResults;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Rayis
 */
@Repository
public class InventoryInOutDao extends BaseQuery{
    
      @Transactional
      public Object getActualStock(InventoryInOut temp) {
        try {

          String qry = " "
            + " SELECT "
            + "	header.Code, "
            + "	header.WarehouseCode "
            + "	header.RackCode, "
            + "	header.ItemCode, "
            + "	header.ActualStock "
            + " FROM "
            + "	mst_item_jn_current_stock header"
            + " WHERE "
            + " header.WarehouseCode = '" + temp.getWarehouseCode() + "' "
            + " AND header.RackCode = '" + temp.getRackCode() + "' "
            + " AND header.ItemCode = '" + temp.getItemCode() + "' ";

          return singleResult(qry);
        } catch (Exception e) {
          e.printStackTrace();
          return null;
        }
      }

      @Transactional
      public Object getActualStockIO(InventoryInOut temp) {
        try {

          String qry = " "
            + " SELECT "
            + "	header.ActualStock "
            + " FROM "
            + "	mst_item_jn_current_stock header"
            + " WHERE "
            + " header.WarehouseCode = '" + temp.getWarehouseCode() + "' "
            + " AND header.RackCode = '" + temp.getRackCode() + "' "
            + " AND header.ItemCode = '" + temp.getItemCode() + "' ";

          return singleResult(qry);
        } catch (Exception e) {
          e.printStackTrace();
          return null;
        }
      }

      @Transactional
      public int countActualStock(InventoryInOut temp) {
        try {

          String qry = " "
            + " SELECT "
            + "	COUNT(header.Code) "
            + " FROM "
            + "	mst_item_jn_current_stock header"
            + " WHERE "
            + " header.WarehouseCode = '" + temp.getWarehouseCode() + "' "
            + " AND header.RackCode = '" + temp.getRackCode() + "' "
            + " AND header.ItemCode = '" + temp.getItemCode() + "' ";

          Long countData = countResult(qry);

          return countData.intValue();

        } catch (Exception e) {
          e.printStackTrace();
          return 0;
        }
      }

      @Transactional
      public void increaseIO(InventoryInOut temp) {
        try {

          int total = countActualStock(temp);
          SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

          if (total > 0) {
            // Update data Di current Stock

            String qry = "UPDATE mst_item_jn_current_stock "
              + " SET "
              + " mst_item_jn_current_stock.ActualStock = (mst_item_jn_current_stock.ActualStock + '" + temp.getActualStock() + "')  "
              + " WHERE "
              + " mst_item_jn_current_stock.WarehouseCode = '" + temp.getWarehouseCode() + "' "
              + " AND mst_item_jn_current_stock.RackCode = '" + temp.getRackCode() + "' "
              + " AND mst_item_jn_current_stock.ItemCode = '" + temp.getItemCode() + "' ";

            session.createNativeQuery(qry).executeUpdate();
          } else {
            // Insert data Di current Stock
            String qry = "INSERT INTO mst_item_jn_current_stock(Code,WarehouseCode,ItemCode,RackCode,ActualStock) "
              + "  SELECT "
              + "  CONCAT('" + temp.getWarehouseCode() + "','-','" + temp.getItemCode() + "','-','" + temp.getRackCode() + "') AS code, "
              + "  '" + temp.getWarehouseCode() + "', "
              + "  '" + temp.getItemCode() + "', "
              + "  '" + temp.getRackCode() + "', "
              + "  '" + temp.getActualStock() + "' ";
            session.createNativeQuery(qry).executeUpdate();
          }

        } catch (Exception e) {
          e.printStackTrace();
        }
      }
      
        @Transactional
        public Boolean decreaseIO(InventoryInOut temp) {
          try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Boolean valid = false;
            int total = countActualStock(temp);

            if (total > 0) {

              Object obejectActualStock = getActualStockIO(temp);

              HashMap<String, BigDecimal> stock = (HashMap<String, BigDecimal>) obejectActualStock;

              BigDecimal actualStock = stock.get("ActualStock");

              int com = actualStock.compareTo(temp.getActualStock());

              if (com < 0) {
                valid = true;
              } else {
                String qry = "UPDATE mst_item_jn_current_stock "
                  + " SET "
                  + " mst_item_jn_current_stock.ActualStock = (mst_item_jn_current_stock.ActualStock - '" + temp.getActualStock() + "')  "
                  + " WHERE "
                  + " mst_item_jn_current_stock.WarehouseCode = '" + temp.getWarehouseCode() + "' "
                  + " AND mst_item_jn_current_stock.RackCode = '" + temp.getRackCode() + "' "
                  + " AND mst_item_jn_current_stock.ItemCode = '" + temp.getItemCode() + "' ";

                session.createNativeQuery(qry).executeUpdate();
              }
            } else {
              valid = true;
            }

            return valid;

          } catch (Exception e) {
            e.printStackTrace();
            return false;
          }
        }

      @Transactional
      public Object defaultRackIn(String warehouseCode) {
        try {
          String qry = " "
            + " SELECT "
            + "	mst_warehouse.Code AS warehouseCode, "
            + "	mst_warehouse.Name AS warehouseName, "
            + "	mst_rack.Code AS rackCode, "
            + "	mst_rack.Code AS rackName "
            + "FROM "
            + "	mst_warehouse "
            + "INNER JOIN mst_rack ON mst_rack.Code = mst_warehouse.DOCK_IN_Code "
            + "WHERE "
            + "	mst_warehouse.Code = '"+warehouseCode+"' ";
          return singleResult(qry);
        } catch (Exception e) {
          e.printStackTrace();
          return null;
        }
      }

      @Transactional
      public Object defaultRackOut(String warehouseCode) {
        try {
          String qry = " "
            + " SELECT "
            + "	mst_warehouse.Code AS warehouseCode, "
            + "	mst_warehouse.Name AS warehouseName, "
            + "	mst_rack.Code AS rackCode, "
            + "	mst_rack.Code AS rackName "
            + "FROM "
            + "	mst_warehouse "
            + "INNER JOIN mst_rack ON mst_rack.Code = mst_warehouse.DOCK_DLN_Code "
            + "WHERE "
            + "	mst_warehouse.Code = '"+warehouseCode+"' ";
          return singleResult(qry);
        } catch (Exception e) {
          e.printStackTrace();
          return null;
        }
      }
    
}
