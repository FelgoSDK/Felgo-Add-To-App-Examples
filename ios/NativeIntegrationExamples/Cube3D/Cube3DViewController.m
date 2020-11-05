//
//  ARViewController.m
//  NativeIntegrationExamples
//
//  Created by Christian Bartsch on 17.04.20.
//  Copyright © 2020 Christian Bartsch. All rights reserved.
//

#import "Cube3DViewController.h"

#import "FelgoIOSView.h"

@interface Cube3DViewController ()

@property (weak, nonatomic) IBOutlet FelgoIOSView *felgoView;

@end

@implementation Cube3DViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    
    self.felgoView.qmlSource = [[NSBundle mainBundle] URLForResource:@"Cube3d" withExtension:@"qml" subdirectory:@"qml"];
}

@end
