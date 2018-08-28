package com.semicolon.criuse.SingleTones;

import android.util.Log;

import com.semicolon.criuse.Models.Bill_Model;
import com.semicolon.criuse.Models.ItemsModel;

import java.util.ArrayList;
import java.util.List;

public class ItemsSingleTone {
    private static ItemsSingleTone instance=null;
    private  List<ItemsModel> itemsModelList;
    private  List<ItemsModel> minimarketItemsList;
    private  List<ItemsModel> superimarketItemsList;
    private  List<Bill_Model> bill_modelList;

    private ItemsSingleTone() {
        itemsModelList = new ArrayList<>();
        minimarketItemsList = new ArrayList<>();
        superimarketItemsList = new ArrayList<>();
        bill_modelList= new ArrayList<>();
    }

    public static synchronized ItemsSingleTone getInstance()
    {
        if (instance==null)
        {
            instance = new ItemsSingleTone();
        }
        return instance;
    }

    public void SaveItem_To_Trolley(ItemsModel itemsModel)
    {
        /*if (itemsModelList.size()>0)
        {
            for (ItemsModel itemsModel1:itemsModelList)
            {
                if (itemsModel1.getMarket_id_fk().equals(itemsModel.getMarket_id_fk())
                        &&
                        itemsModel1.getId_product().equals(itemsModel.getId_product())
                        )
                {
                    int index = itemsModelList.indexOf(itemsModel1);
                    int costoneitem=Integer.parseInt(itemsModel.getItem_one_cost());
                    int oldamount = Integer.parseInt(itemsModel.getProduct_amount());
                    int newamount = Integer.parseInt(itemsModel1.getProduct_amount());

                    int totalamount = oldamount+newamount;
                    int totalcost = totalamount*costoneitem;
                    itemsModel.setProduct_cost(String.valueOf(totalcost));
                    itemsModel.setProduct_amount(String.valueOf(totalamount));
                    itemsModelList.set(index,itemsModel);
                }else
                    {
                        itemsModelList.add(itemsModel);

                    }
            }


        }else
            {
                itemsModelList.add(itemsModel);
            }*/
        if (itemsModelList.contains(itemsModel))
        {
            Log.e("contain","true");
            int index = itemsModelList.indexOf(itemsModel);
            itemsModelList.set(index,itemsModel);
            Log.e("list_trolley_size1",itemsModelList.size()+"");
        }else
        {
            Log.e("contain","false");

            itemsModelList.add(itemsModel);
            Log.e("list_trolley_size2",itemsModelList.size()+"");

        }
    }

    public void RemoveItem_From_Trolley(ItemsModel itemsModel)
    {
        itemsModelList.remove(itemsModel);
    }

    public List<ItemsModel> getItemsModelList()
    {
        return itemsModelList;
    }

    public void setMinimarketItemsList(List<ItemsModel> minimarketItemsList,String user_id)
    {
        List<ItemsModel> itemsModelList = new ArrayList<>();
        for (ItemsModel itemsModel1:minimarketItemsList)
        {
            Log.e("idddddd",user_id);
            itemsModel1.setUser_id(user_id);
            itemsModelList.add(itemsModel1);
        }
        this.minimarketItemsList=itemsModelList;
    }

    public List<ItemsModel> getMinimarketItemsList()
    {
        return this.minimarketItemsList;
    }

    public void setSupermarketItemsList(List<ItemsModel> supermarketItemsList,String user_id)
    {
        List<ItemsModel> itemsModelList = new ArrayList<>();
        for (ItemsModel itemsModel1:supermarketItemsList)
        {
            Log.e("idddddd",user_id+"___");
            Log.e("fk",itemsModel1.getMarket_name()+"_");

            itemsModel1.setUser_id(user_id);
            itemsModelList.add(itemsModel1);
        }
        this.superimarketItemsList=itemsModelList;
    }

    public List<ItemsModel> getSupermarketItemsList()
    {
        return this.superimarketItemsList;
    }

    public void setBill_modelList(List<Bill_Model> bill_modelList)
    {
        this.bill_modelList=bill_modelList;
    }

    public List<Bill_Model> getBill_modelList()
    {
        return this.bill_modelList;
    }
    public void ClearItemModelList()
    {
        itemsModelList.clear();
        minimarketItemsList.clear();
        superimarketItemsList.clear();
        bill_modelList.clear();
        Location_Order_SingleTone location_order_singleTone = Location_Order_SingleTone.getInstance();
        location_order_singleTone.clearLocation();
    }

    public void  clear()
    {
        minimarketItemsList.clear();
        superimarketItemsList.clear();
        bill_modelList.clear();
    }


}
