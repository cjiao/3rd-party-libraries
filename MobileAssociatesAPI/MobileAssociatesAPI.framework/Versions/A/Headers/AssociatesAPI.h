//
//  MAAIOSSDKDirectShopping.h
//  MAAIOSSDKDirectShopping
//
//  Created by Singh, Prashant Bhushan on 10/22/13.
//  Copyright (c) 2013 amazon.com. All rights reserved.
//
//  Entry header file for all exposed API of Mobile Associates

#import "LinkService.h"

#import "OpenProductPageRequest.h"
#import "OpenSearchPageRequest.h"
#import "OpenHomePageRequest.h"
#import "MAAServiceStatusRequest.h"
#import "MAAServiceStatusResponse.h"

// This class is used to provide the API configuration to [AssociatesAPI initializeWithConfig]
// Holds the configuration details required for initialization of Mobile Associates API.
@interface AssociatesAPIConfig : NSObject

// Application Key, it's unique for each developer and must be generated at http://developer.amazon.com
@property (nonatomic, readonly) NSString *appKey;

// Array of Product Ids (NSString *), whose data will be prefetched at the API initialization time.
// Prefetching of data of products can significantly improve the performance of API.
// The maximum supported size of Array is 20.
@property (nonatomic) NSArray *prefetchProductList;

// Initialization message call for AssociatesAPIConfig class
- (AssociatesAPIConfig *)initWithAppKey:(NSString *)applicationKey;

@end


// This class initializes the Mobile Associates API, and prepares it for use in the App.
@interface AssociatesAPI : NSObject

// Initialization message call for AssociatesAPI class,
// Raises exception if the argument passed to it is invalid or nil
// This message must be called from applicationDelegate's didFinishLaunchingWithOptions method
// calling linkService without initialization will raise MAANotInitializedException
+ (void)initializeWithConfig:(AssociatesAPIConfig *) config;

// Return LinkService singleton instance.
+ (LinkService *) linkService;

// To determine API status for various use cases.
// e.g. LinkService.openRetailPage(OpenProductPageRequest request) etc.
+ (MAAServiceStatusResponse *) serviceStatusWithRequest: (MAAServiceStatusRequest *) request;
@end