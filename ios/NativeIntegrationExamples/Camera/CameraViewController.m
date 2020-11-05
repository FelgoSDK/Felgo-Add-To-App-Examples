//
//  SecondViewController.m
//  NativeIntegrationExamples
//
//  Created by Christian Bartsch on 16.04.20.
//  Copyright © 2020 Christian Bartsch. All rights reserved.
//

#import "CameraViewController.h"

#import "FelgoIOSView.h"

@interface CameraViewController ()

@property (weak, nonatomic) IBOutlet FelgoIOSView *felgoView;
@property (weak, nonatomic) IBOutlet UIButton *captureButton;
@property (weak, nonatomic) IBOutlet UIImageView *imageView;

- (void)imageCaptured:(NSString *)imageUrl;

@end

@implementation CameraViewController

- (void)viewDidLoad {
    [super viewDidLoad];
}

- (IBAction)onCaptureProfilePictureClicked:(id)sender {
    
    QMLSignalHandler imageCaptureHandler = ^(NSArray * _Nonnull params) {
        NSString *imagePath = params[0];
        
        [self imageCaptured:imagePath];
    };
        
    self.felgoView.qmlInitBlock = ^{        
        [self.felgoView addSignalHandler:@"imageCaptured" handler:imageCaptureHandler];
    };

    self.felgoView.qmlSource = [[NSBundle mainBundle] URLForResource:@"Camera" withExtension:@"qml" subdirectory:@"qml"];
    
    self.imageView.hidden = YES;
    self.captureButton.hidden = YES;
    self.felgoView.hidden = NO;
}

- (void)imageCaptured:(NSString *)imagePath {
    NSLog(@"imageCaptured %@", imagePath);
    
    self.imageView.hidden = NO;
    self.captureButton.hidden = NO;
    self.felgoView.hidden = YES;
    self.felgoView.qmlSource = [NSURL URLWithString:@""];
    
    self.imageView.image = [UIImage imageWithContentsOfFile:imagePath];
}

@end
