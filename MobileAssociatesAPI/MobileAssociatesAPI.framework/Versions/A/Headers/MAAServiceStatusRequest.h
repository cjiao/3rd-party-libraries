//
//  MAAServiceStatusRequest.h
//  MAAIOSSDKDirectShopping
//
//  Created by K, Vinay on 8/1/14.
//  Copyright (c) 2014 amazon.com. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface MAAServiceStatusRequest : NSObject

@property(nonatomic, readonly) NSArray *productIds;

// The override init method.
- (id) initWithProductIds: (NSArray *) prodIds;

@end
