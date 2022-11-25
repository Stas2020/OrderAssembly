package com.westas.orderassembly.invoice;

import com.google.gson.annotations.SerializedName;

public class Claim {
    @SerializedName("description_claim")
    public String description_claim;
    @SerializedName("num_claim_external")
    public String num_claim_external;
}
