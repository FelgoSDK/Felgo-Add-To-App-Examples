//
//  SensorsViewController.m
//  NativeIntegrationExamples
//
//  Created by Christian Bartsch on 22.04.20.
//  Copyright © 2020 Christian Bartsch. All rights reserved.
//

#import "SensorsViewController.h"

#import "FelgoIOSView.h"

@interface SensorsViewController ()

@property (weak, nonatomic) IBOutlet FelgoIOSView *felgoView;

@property (weak, nonatomic) IBOutlet UITextView *textView;

@end

@implementation SensorsViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    
    QMLSignalHandler readingHandler = ^(NSArray * _Nonnull params) {
        [self updateSensorText];
    };
    
    self.felgoView.qmlInitBlock = ^{
        [self.felgoView addSignalHandler:@"onCurrentReadingChanged" handler:readingHandler];
    };

    self.felgoView.qmlSource = [[NSBundle mainBundle] URLForResource:@"Sensors" withExtension:@"qml" subdirectory:@"qml"];
}

- (void)updateSensorText {
    NSDictionary *reading = [self.felgoView getQmlProperty:@"currentReading"];
    
    self.textView.text = [NSString stringWithFormat:@"Sensor reading: %@", reading];
}

@end
