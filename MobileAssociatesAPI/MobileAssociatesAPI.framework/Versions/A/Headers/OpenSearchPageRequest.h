//
//  MAAOpenSearchPage.h
//  MAAIOSSDKDirectShopping
//
//  Created by Singh, Prashant Bhushan on 10/26/13.
//  Copyright (c) 2013 amazon.com. All rights reserved.
//

#import "OpenRetailPageRequest.h"
#import "Commons.h"

// Object of OpenSearchPageRequest is passed to [LinkService openRetailPage] to
// build a link to amazon retail search page with products in Amazon product catalog using keywords and category. To
// initiate link building for retail search page, specify the keyword, category, sort type and brand.
@interface OpenSearchPageRequest : OpenRetailPageRequest

@property (nonatomic) NSString *searchTerm;
@property (nonatomic) NSString *category;
@property (nonatomic) NSString *brand;
@property (nonatomic) MAASortType sortType;

// Initializes a new OpenSearchPageRequest for the search key word specified as parameter.
// Passing nil or empty search key word will raise invalid argument exception.
- (id) initWithSearchTerm: (NSString *) searchTerm;

// Initializes a new OpenSearchPageRequest for the search key word and category specified as parameter.
// Passing nil or empty search key word or category will raise invalid argument exception.
- (id) initWithSearchTerm: (NSString *) searchTerm andCategory: (NSString *) category;

@end
