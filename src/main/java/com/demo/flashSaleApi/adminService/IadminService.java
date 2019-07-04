package com.demo.flashSaleApi.adminService;

import com.demo.flashSaleApi.model.FlashSale;
import com.demo.flashSaleApi.model.Product;

public interface IadminService {
	 
	    FlashSale createFlashSale(final Product p);

	    
	    Boolean startFlashSale(final FlashSale f);

	    
	    Boolean stopFlashSale(final FlashSale f);
	}

