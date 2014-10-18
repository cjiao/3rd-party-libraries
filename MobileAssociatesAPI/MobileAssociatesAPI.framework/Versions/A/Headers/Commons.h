//
//  Commons.h
//  MAAIOSSDKDirectShopping
//
//  Created by Vaidya, Ashish on 12/30/13.
//  Copyright (c) 2013 amazon.com. All rights reserved.
//

#ifndef MAAIOSSDKDirectShopping_Commons_h
#define MAAIOSSDKDirectShopping_Commons_h

// Represents a type of sort that can be passed to OpenSearchPageRequest or Discovery calls.
// Setting the sort type in OpenSearchPageRequest object opens the amazon retail search page
// or returns discovery response with the products sorted in specified format.
typedef enum {
    // Sorts search results based on price (low-to-high).
    PRICE_LOW_TO_HIGH = 1,
    
    // Sorts search results based on price (high-to-low).
    PRICE_HIGH_TO_LOW = 2,
    
    // Sort search results based on relevance. This is the default sort order.
    RELEVANCE = 3
    
} MAASortType;

#endif
