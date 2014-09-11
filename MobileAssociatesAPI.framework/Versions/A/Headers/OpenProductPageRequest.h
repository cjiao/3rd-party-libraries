//
//  MAAOpenProductDetailPage.h
//  MAAIOSSDKDirectShopping
//
//  Created by Singh, Prashant Bhushan on 10/26/13.
//  Copyright (c) 2013 amazon.com. All rights reserved.
//

#import "OpenRetailPageRequest.h"

// Object of OpenProductPageRequest is passed to [LinkService openRetailPage] to build a link to the Amazon retail web.
// To initiate link building of a specific product, specify it's Product ID in the OpenProductPageRequest.
@interface OpenProductPageRequest : OpenRetailPageRequest

@property(nonatomic) NSString *productId;

// Initializes a new OpenProductPageRequest for the product ID specified as parameter.
// Passing nil or empty product Id will raise invalid argument exception.
- (id) initWithProductId: (NSString *) prodId;

@end
