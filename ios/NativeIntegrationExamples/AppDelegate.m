//
//  AppDelegate.m
//  NativeIntegrationExamples
//
//  Created by Christian Bartsch on 16.04.20.
//  Copyright © 2020 Christian Bartsch. All rights reserved.
//

#import "AppDelegate.h"

#import "FelgoIOS.h"

@interface AppDelegate ()

@end

@implementation AppDelegate


- (BOOL)application:(UIApplication *)application didFinishLaunchingWithOptions:(NSDictionary *)launchOptions {
    [[FelgoIOS sharedInstance] start];
    
    return YES;
}

- (void)applicationWillTerminate:(UIApplication *)application {
    [[FelgoIOS sharedInstance] quit];
}


@end
