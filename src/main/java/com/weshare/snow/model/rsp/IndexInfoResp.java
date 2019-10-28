package com.weshare.snow.model.rsp;

import com.weshare.snow.model.CtcAdvet;
import com.weshare.snow.model.CtcPrroduct;
import com.weshare.snow.model.CtcPrroductCategory;
import com.weshare.snow.model.CtcStriking;

import java.util.ArrayList;
import java.util.List;

/**
 * @Auther: CTC
 * @Date: 2019/10/25 12:06
 * @Description:
 */
public class IndexInfoResp {

    private List<CtcAdvet> ctcadvets;

    public List<CtcAdvet> getCtcadvets() {
        return ctcadvets == null ? new ArrayList<CtcAdvet>() : ctcadvets;
    }

    public void setCtcadvets(List<CtcAdvet> ctcadvets) {
        this.ctcadvets = ctcadvets;
    }

    private List<CtcStriking> CtcStrikings;

    public List<CtcStriking> getCtcStrikings() {
        return CtcStrikings == null ? new ArrayList<CtcStriking>() : CtcStrikings;
    }

    public void setCtcStrikings(List<CtcStriking> ctcStrikings) {
        CtcStrikings = ctcStrikings;
    }

    private List<CtcPrroductCategory> ctcPrroductCategorys;

    public List<CtcPrroductCategory> getCtcPrroductCategorys() {
        return ctcPrroductCategorys == null ? new ArrayList<CtcPrroductCategory>() : ctcPrroductCategorys;
    }

    public void setCtcPrroductCategorys(List<CtcPrroductCategory> ctcPrroductCategorys) {
        this.ctcPrroductCategorys = ctcPrroductCategorys;
    }

    private List<CtcPrroduct> newProducts;

    public List<CtcPrroduct> getNewProducts() {
        return newProducts == null ? new ArrayList<CtcPrroduct>() : newProducts;
    }

    public void setNewProducts(List<CtcPrroduct> newProducts) {
        this.newProducts = newProducts;
    }

    private List<CtcPrroduct> products;

    public List<CtcPrroduct> getProducts() {
        return products== null ? new ArrayList<CtcPrroduct>() : products;
    }

    public void setProducts(List<CtcPrroduct> products) {
        this.products = products;
    }
}
