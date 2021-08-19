package com.westas.orderassembly.invoice_items;



import com.google.gson.annotations.SerializedName;

enum SatusQuantity {less, equally, over, default_}
enum SatusItem {add, delete, def}


public class InvoiceItem implements Comparable<InvoiceItem>{

    @SerializedName("uid")
    private String uid ;
    @SerializedName("uid_invoice")
    private String uid_invoice ;
    @SerializedName("barcode")
    private String barcode;
    @SerializedName("name")
    private String name;
    @SerializedName("quantity")
    private float quantity;
    @SerializedName("required_quantity")
    private float required_quantity;
    @SerializedName("unit")
    private String unit;
    @SerializedName("status")
    private SatusItem status;
    @SerializedName("synchronized")
    private boolean synchronized_;
    @SerializedName("list_box")
    private java.util.List<Box> list_box;

    private boolean selected = false;

    public SatusItem GetStatus()
    {
        return status;
    }
    public boolean CheckSynchronized()
    {
        return synchronized_;
    }
    public boolean CheckSelected()
    {
        return selected;
    }
    public void UnSelected()
    {
        selected = false;
    }
    public void Select()
    {
        selected = true;
    }
    public String GetUid()
    {
        return uid;
    }
    public String GetUidInvoice(){return uid_invoice;}
    public String GetBarcode()
    {
        return barcode;
    }
    public String GetName()
    {
        return name;
    }
    public String GetUnit()
    {
        return unit;
    }
    boolean CheckChanged()
    {
        if(quantity > 0)
        {
            return true;
        }
        return false;
    }

    SatusQuantity GetStatusQuantity()
    {
        SatusQuantity result = SatusQuantity.default_;

        if (quantity > required_quantity && quantity != 0)
        {
            result = SatusQuantity.over;
        }
        if (quantity == required_quantity && quantity != 0)
        {
            result = SatusQuantity.equally;
        }
        if (quantity < required_quantity && quantity != 0)
        {
            result = SatusQuantity.less;
        }

        return result;
    }

    public Box GetBox(String barcode)
    {
        for(Box box : list_box)
        {
            if(box.barcode.equals(barcode))
            {
                return box;
            }
        }
        return null;
    }

    public void SetQuantity(float value)
    {
        quantity = value;
    }

    public float GetQuantity()
    {
        return quantity;
    }

    public float GetRequiredQuantity()
    {
        return required_quantity;
    }

    @Override
    public int compareTo(InvoiceItem item) {

        if (this.CheckChanged() == true && item.CheckChanged() == false)
        {
            return -1;
        }
        if (this.CheckChanged() == true && item.CheckChanged() == true)
        {
            return 0;
        }
        if (this.CheckChanged() == false && item.CheckChanged() == true)
        {
            return 1;
        }


/*
        if ( (this.verify == SatusQuantity.equally || this.verify == SatusQuantity.over || this.verify == SatusQuantity.less) &&
                (item.verify != SatusQuantity.equally || item.verify != SatusQuantity.over || item.verify != SatusQuantity.less))
        {
            return -1;
        }

        if ( (this.verify == SatusQuantity.equally || this.verify == SatusQuantity.over || this.verify == SatusQuantity.less) &&
                (item.verify == SatusQuantity.equally || item.verify == SatusQuantity.over || item.verify == SatusQuantity.less))
        {
            return 0;
        }

        if ( (this.verify != SatusQuantity.equally || this.verify != SatusQuantity.over || this.verify != SatusQuantity.less) &&
                (item.verify == SatusQuantity.equally || item.verify == SatusQuantity.over || item.verify == SatusQuantity.less))
        {
            return 1;
        }
*/


        return 0;


    }
}

