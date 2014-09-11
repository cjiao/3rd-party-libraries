//
//  MAANotInitializedException.h
//  MAAIOSSDKDirectShopping
//
//  Created by Singh, Prashant Bhushan on 11/27/13.
//  Copyright (c) 2013 amazon.com. All rights reserved.
//

#import <Foundation/Foundation.h>

// This exception will be raised, if the MobileAssociates API is used without
// initialization. Mobile Associates API should always be properly initialized
// in AppDelegate's didFinishLaunchingWithOptions method.
@interface MAANotInitializedException : NSException

@end
