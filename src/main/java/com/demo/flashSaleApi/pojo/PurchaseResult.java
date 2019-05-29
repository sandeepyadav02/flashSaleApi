package com.demo.flashSaleApi.pojo;

public class PurchaseResult {
	private Boolean status;

    private Integer buyerId;

    public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

	public PurchaseResult(Boolean status, Integer buyerId, Integer productId) {
		super();
		this.status = status;
		this.buyerId = buyerId;
		this.productId = productId;
	}

	public Integer getBuyerId() {
		return buyerId;
	}

	public void setBuyerId(Integer buyerId) {
		this.buyerId = buyerId;
	}

	public Integer getProductId() {
		return productId;
	}

	public void setProductId(Integer productId) {
		this.productId = productId;
	}

	private Integer productId;
}
