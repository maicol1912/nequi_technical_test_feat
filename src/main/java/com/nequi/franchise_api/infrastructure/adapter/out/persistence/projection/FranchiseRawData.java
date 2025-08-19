package com.nequi.franchise_api.infrastructure.adapter.out.persistence.projection;

public class FranchiseRawData {

    private String franchiseId;
    private String franchiseName;
    private String branchId;
    private String branchName;
    private String productId;
    private String productName;
    private Integer productStock;


    public FranchiseRawData(String franchiseId, String franchiseName, String branchId, String branchName,
                            String productId, String productName, Integer productStock) {
        this.franchiseId = franchiseId;
        this.franchiseName = franchiseName;
        this.branchId = branchId;
        this.branchName = branchName;
        this.productId = productId;
        this.productName = productName;
        this.productStock = productStock;
    }

    // Getters y setters
    public String getFranchiseId() {
        return franchiseId;
    }

    public void setFranchiseId(String franchiseId) {
        this.franchiseId = franchiseId;
    }

    public String getFranchiseName() {
        return franchiseName;
    }

    public void setFranchiseName(String franchiseName) {
        this.franchiseName = franchiseName;
    }

    public String getBranchId() {
        return branchId;
    }

    public void setBranchId(String branchId) {
        this.branchId = branchId;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Integer getProductStock() {
        return productStock;
    }

    public void setProductStock(Integer productStock) {
        this.productStock = productStock;
    }
}

