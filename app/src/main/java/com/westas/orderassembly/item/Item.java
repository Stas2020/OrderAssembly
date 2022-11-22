package com.westas.orderassembly.item;

import com.google.gson.annotations.SerializedName;

enum StatusQuantity {less, equally, over, default_}
// для сборки заказов
enum StatusItem {add, delete, def}

public class Item implements Comparable<Item>{

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
    private StatusItem status;
    @SerializedName("synchronized")
    private boolean synchronized_;
    @SerializedName("uid_box")
    private String uid_box;
    @SerializedName("list_package")
    private java.util.List<Package_> list_package;

    private boolean selected = false;

    //@SerializedName("skip_reason")
    private StatusSkip statusSkip = StatusSkip.none;
    //@SerializedName("skip_notes")
    private String skipNotes;

    public StatusItem GetStatus()
    {
        return status;
    }
    public StatusSkip GetStatusSkip(){
        return statusSkip;
    }
    public String GetSkipNotes(){
        return skipNotes;
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

    StatusQuantity GetStatusQuantity()
    {
        StatusQuantity result = StatusQuantity.default_;

        if (quantity > required_quantity && quantity != 0)
        {
            result = StatusQuantity.over;
        }
        if (quantity == required_quantity && quantity != 0)
        {
            result = StatusQuantity.equally;
        }
        if (quantity < required_quantity && quantity != 0)
        {
            result = StatusQuantity.less;
        }

        return result;
    }

    public Package_ GetPackage(String barcode)
    {
        for(Package_ package_ : list_package)
        {
            if(package_.barcode.equals(barcode))
            {
                return package_;
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

    public String GetBoxUid()
    {
        return uid_box;
    }

    @Override
    public int compareTo(Item item) {

        if ((this.CheckChanged() == true || this.CheckAdded()) && item.CheckChanged() == false)
        {
            return -1;
        }
        if ((this.CheckChanged() == true || this.CheckAdded()) && item.CheckChanged() == true)
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

    private boolean CheckAdded() {
        return status == StatusItem.add;
    }
}

