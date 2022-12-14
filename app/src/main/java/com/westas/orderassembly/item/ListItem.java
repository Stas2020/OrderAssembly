package com.westas.orderassembly.item;

import com.google.gson.annotations.SerializedName;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ListItem {
    @SerializedName("list_item")
    private List<Item> list;

    public void SortingItem()
    {
        Collections.sort(list);
        //MoveAddedItemsToLastPosition();
    }

    public void SortingItemByCode()
    {
        Collections.sort(list, Comparator.comparing(Item::GetBarcode));
    }

    public void SortingItemByName()
    {
        Collections.sort(list, Comparator.comparing(Item::GetName));
    }

    public  void  MovePositionToLastPlace(Item item)
    {
        int last_position = GetLastPosition();

        Item last_item = GetItems(last_position);
        if (item != last_item)
        {
            int position = list.indexOf(item);
            if(position > last_position)
            {
                list.remove(position);
                list.add(last_position,item);
            }

        }
    }

    public int GetSelectedPosition()
    {
        int pos = 0;
        for (Item itm:list) {
            if(itm.CheckSelected())
            {
                return pos;
            }
            ++pos;
        }

        return -1;
    }
    public int GetLastPosition()
    {
        int pos = 0;
        for (Item itm:list) {
            if(!itm.CheckChanged())
            {
                return pos;
            }
            ++pos;
        }
        return pos - 1;
    }

    private void ClearCelectedItem()
    {
        for (Item item:list) {
            item.UnSelected();
        }
    }

    public void SelectItem(Item item)
    {
        ClearCelectedItem();
        item.Select();
    }

    public void Remove(int idx)
    {
        list.remove(idx);
    }

    public void Add(Item item, int idx)
    {
        list.add(idx, item);
    }

    public Item GetItems(int position)
    {
        return list.get(position);
    }

    public Item GetItemsByUid(String uid)
    {
        for (Item item:list) {
            if(item.GetUid().equals(uid))
            {
                return item;
            }
        }
        return null;
    }

    public Item GetItemsByBarcode(String barcode)
    {
        for (Item item:list) {
            if(item.GetBarcode().equals(barcode) || item.GetPackage(barcode) != null)
            {
                return item;
            }
        }
        return null;
    }

    public boolean CheckAllItemSynchronized()
    {
        for (Item item:list) {

            if(item.CheckSynchronized() == false)
            {
                return false;
            }
        }
        return true;
    }

    public int GetSize()
    {
        return list.size();
    }

    public boolean CheckSelectedPosition(int position)
    {
        return list.get(position).CheckSelected();
    }

    private TOnChangeQuantity event_change_quantity;

    public void SetEventOfChangeQuantity(TOnChangeQuantity value)
    {
        event_change_quantity = value;
    }

/*
    private void MoveAddedItemsToLastPosition()
    {
        ArrayList<InvoiceItem> list_added = new ArrayList<InvoiceItem>();

        for (InvoiceItem item:list) {
            if(item.status == add)
            {
                list_added.add(item);
            }
        }

        for (InvoiceItem item:list_added)
        {
            int pos = list.indexOf(item);
            if(pos != -1)
            ChangePositionToLastSelect(pos);
        }

    }


 */


    /*
        public List<InvoiceItem> GetItemsAll()
        {
            return st;
        }
    /*
        public List<InvoiceItem> GetItemsNotSynchronized()
        {
            ArrayList<InvoiceItem> items = new ArrayList<InvoiceItem>();
            for(InvoiceItem item:list)
            {
                if(item.synchronized_ == false)
                {
                    items.add(item);
                }
            }
            return items;
        }


     */

    /*
    public boolean VerifyGoods(double quantity, String code)
    {
        int position = 0;


        for (InvoiceItem itm: list)
        {
            if (itm.barcode.equals(code))
            {
                itm.quantity += quantity;
                itm.quantity = itm.required_quantity;

                if (event_change_quantity != null)
                    event_change_quantity.EventChangeQuantity(itm.uid, itm.barcode, itm.quantity);

                //???????????? ?????????????? ???????????????????? item
                ChangePositionToLastSelect(position);
                ChangeStatusQuantity(itm);
                return true;
            }

            for (Box box : itm.list_box)
            {
                if (box.barcode.equals(code))
                {
                    double qt = box.quantity_in_box * quantity;
                    itm.quantity += qt;
                    //itm.quantity = itm.required_quantity;

                    if (event_change_quantity != null)
                        event_change_quantity.EventChangeQuantity(itm.uid, itm.barcode, itm.quantity);

                    //???????????? ?????????????? ???????????????????? item
                    ChangePositionToLastSelect(position);
                    ChangeStatusQuantity(itm);
                    return true;
                }
            }

            position++;
        }

        return false;
    }



    public void ChangeQuantity(float quantity, InvoiceItem item)
    {
        int position = list.indexOf(item);

        if(position != -1)
        {
            list.get(position).quantity += quantity;
            if (event_change_quantity != null)
                event_change_quantity.EventChangeQuantity(list.get(position).uid, list.get(position).barcode, list.get(position).quantity);
            //???????????? ?????????????? ???????????????????? item
            ChangePositionToLastSelect(position);
        }



        for (InvoiceItem itm: list)
        {
            if (itm.barcode.equals(code))
            {
                itm.quantity += quantity;
                itm.quantity = itm.required_quantity;

                if (event_change_quantity != null)
                    event_change_quantity.EventChangeQuantity(itm.uid, itm.barcode, itm.quantity);

                //???????????? ?????????????? ???????????????????? item
                ChangePositionToLastSelect(position);
                ChangeStatusQuantity(itm);
                return true;
            }

            for (Box box : itm.list_box)
            {
                if (box.barcode.equals(code))
                {
                    double qt = box.quantity_in_box * quantity;
                    itm.quantity += qt;
                    //itm.quantity = itm.required_quantity;

                    if (event_change_quantity != null)
                        event_change_quantity.EventChangeQuantity(itm.uid, itm.barcode, itm.quantity);

                    //???????????? ?????????????? ???????????????????? item
                    ChangePositionToLastSelect(position);
                    ChangeStatusQuantity(itm);
                    return true;
                }
            }

            position++;
        }

    }



    public void ChangeQuantity(float quantity, String uid)
    {
        int position = 0;
        for (InvoiceItem itm: list)
        {
            if (itm.uid.equals(uid))
            {
                itm.quantity = quantity;

                if (event_change_quantity != null){
                    event_change_quantity.EventChangeQuantity(itm.uid, itm.barcode, itm.quantity);
                }

                //???????????? ?????????????? ???????????????????? item
                Log.i("MESSAGE", "???????????? ?????????????? " + String.valueOf(position) + " ???????????????????? item : ");
                ChangePositionToLastSelect(position);
                //ChangeStatusQuantity(itm);
                break;
            }
            position++;
        }
    }




    // ???????????? ?????????????? ???? ?????????????????? ????????????????????
    private void ChangePositionToLastSelect(int position)
    {
        int idx = GetLastPositionBySelect();
        Log.i("MESSAGE", "id ?????????????????? ?????????????? : " + String.valueOf(idx));
        if(position != 0 && idx < list.size())
        {
            InvoiceItem itm = list.get(position);

            list.remove(position);
            list.add(idx,itm);
        }
    }
 */
    //???????????? ???????????? item
    /*
    private void ChangeStatusQuantity(InvoiceItem itm) {

        if (itm.quantity > itm.required_quantity)
        {
            itm.verify = SatusQuantity.over;
        }
        if (itm.quantity == itm.required_quantity)
        {
            itm.verify = SatusQuantity.equally;
        }
        if (itm.quantity < itm.required_quantity)
        {
            itm.verify = SatusQuantity.less;
        }

    }


 */





}
