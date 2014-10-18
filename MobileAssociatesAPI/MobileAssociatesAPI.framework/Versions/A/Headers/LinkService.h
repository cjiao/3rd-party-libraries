//
//  LinkService.h
//  MAAIOSSDKDirectShopping
//
//  Created by Singh, Prashant Bhushan on 11/25/13.
//  Copyright (c) 2013 amazon.com. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "OpenRetailPageRequest.h"

// This interface provides methods for developers to build links to Amazon products and allow shopping in a browser upon a
// user action.
@interface LinkService : NSObject

// Always returns the same instance (singleton) instance of LinkService
+ (LinkService *)sharedInstance;

// Used to load a Amazon retail webpage for a given OpenRetailPageRequest object.
// OpenRetailPageRequest can be one of below <br>
// <li>OpenProductPageRequest for product page.</li>
// <li>OpenSearchPageRequest for a search page.</li>
// <li>OpenHomePageRequest for the Amazon retail web.</li>
//
// @param request the retail page request.
// @throws IllegalArgumentException if request is null, MAANotInitailizedException if API is not initialized.
- (void)openRetailPage:(OpenRetailPageRequest *)linkEntity;

@end
