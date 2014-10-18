//
//  MAAServiceStatusResponse.h
//  MAAIOSSDKDirectShopping
//
//  Created by K, Vinay on 8/1/14.
//  Copyright (c) 2014 amazon.com. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface MAAServiceStatusResponse : NSObject

// The override init method.
- (id) initWithProductIdsStatusMap: (NSDictionary *) prodStatusMap;

// The method to find out if a product is supported for opening retail page or not.
- (BOOL) isOpenRetailPageSupportedForProductId:(NSString *)productId;

@end
