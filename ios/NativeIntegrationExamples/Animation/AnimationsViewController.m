//
//  AnimationsViewController.m
//  NativeIntegrationExamples
//
//  Created by Christian Bartsch on 22.04.20.
//  Copyright © 2020 Christian Bartsch. All rights reserved.
//

#import "AnimationsViewController.h"

#import "FelgoIOSView.h"

@interface AnimationsViewController ()
@property (weak, nonatomic) IBOutlet FelgoIOSView *felgoView;

@end

@implementation AnimationsViewController

- (void)viewDidLoad {
    [super viewDidLoad];

    self.felgoView.qmlSource = [[NSBundle mainBundle] URLForResource:@"Animations" withExtension:@"qml" subdirectory:@"qml"];
}

- (IBAction)onCreateItemClicked:(id)sender {
    [self.felgoView callQmlMethod:@"createItem" value:@[]];
}

- (IBAction)onAnimateItemsClicked:(id)sender {
    [self.felgoView callQmlMethod:@"animateItems" value:@[]];
}

- (IBAction)onClearItemsClicked:(id)sender {
    [self.felgoView callQmlMethod:@"clearItems" value:@[]];
}

@end
