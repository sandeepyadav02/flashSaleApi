package com.demo.flashSaleApi.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.demo.flashSaleApi.pojo.RegistrationStatus;



@Entity
@Table(name = "registrations")

public class Registration {
	
	@Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    
    private Integer id;

    public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public FlashSale getFlashSale() {
		return flashSale;
	}

	public void setFlashSale(FlashSale flashSale) {
		this.flashSale = flashSale;
	}

	public Buyer getBuyer() {
		return buyer;
	}

	public void setBuyer(Buyer buyer) {
		this.buyer = buyer;
	}

	@OneToOne()
    @JoinColumn(name = "flashsale_id")
    private FlashSale flashSale;

    @OneToOne()
    @JoinColumn(name = "buyer_id")
    private Buyer buyer;

    public RegistrationStatus getRegistrationStatus() {
		return registrationStatus;
	}

	public void setRegistrationStatus(RegistrationStatus registrationStatus) {
		this.registrationStatus = registrationStatus;
	}

	@Enumerated(EnumType.STRING)
    private RegistrationStatus registrationStatus;
    
   

}