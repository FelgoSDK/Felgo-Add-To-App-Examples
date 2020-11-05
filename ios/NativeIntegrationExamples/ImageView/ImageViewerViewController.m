//
//  ImageViewerViewController.m
//  NativeIntegrationExamples
//
//  Created by Christian Bartsch on 21.04.20.
//  Copyright © 2020 Christian Bartsch. All rights reserved.
//

#import "ImageViewerViewController.h"

#import "FelgoIOSView.h"

@interface ImageViewerViewController ()

@property (weak, nonatomic) IBOutlet FelgoIOSView *felgoView;
@property (weak, nonatomic) IBOutlet UITextView *urlTextView;

@property (strong, nonatomic) NSString *imageUrl;

@end

@implementation ImageViewerViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    
    self.urlTextView.delegate = self;
    
    // show image URL in max 2 lines
    self.urlTextView.textContainer.maximumNumberOfLines = 2;
    self.urlTextView.textContainer.lineBreakMode = NSLineBreakByTruncatingMiddle;
    
    self.felgoView.qmlInitBlock = ^{
        if(self.imageUrl == nil || self.imageUrl.length == 0) {
            // set initial image from resource bundle
            self.imageUrl = [[[NSBundle mainBundle] URLForResource:@"felgo-logo" withExtension:@"png" subdirectory:@"img"] absoluteString];
        } else {
            // call setter to update QML Property
            self.imageUrl = self.imageUrl;
        }
    };
    
    self.felgoView.qmlSource = [[NSBundle mainBundle] URLForResource:@"ImageViewer" withExtension:@"qml" subdirectory:@"qml"];
}

- (void)setImageUrl:(NSString *)imageUrl {
    _imageUrl = imageUrl;
    
    self.urlTextView.text = imageUrl;
    
    [self.felgoView setQmlProperty:@"imageUrl" value:self.imageUrl];
}

- (IBAction)onResetTransformClicked:(id)sender {
    [self.felgoView callQmlMethod:@"resetTransform" value:@[]];
}

- (IBAction)onPickImageClicked:(id)sender {
    UIImagePickerController* imagePicker = [[UIImagePickerController alloc] init];
    // Check if image access is authorized
    if([UIImagePickerController isSourceTypeAvailable:UIImagePickerControllerSourceTypePhotoLibrary]) {
        imagePicker.sourceType = UIImagePickerControllerSourceTypePhotoLibrary;
        // Use delegate methods to get result of photo library -- Look up UIImagePicker delegate methods
        imagePicker.delegate = self;
        [self presentViewController:imagePicker animated:true completion:nil];
    }
}

- (void)imagePickerController:(UIImagePickerController *)picker didFinishPickingMediaWithInfo:(NSDictionary *)info {
    NSURL *url = info[UIImagePickerControllerImageURL];
    self.imageUrl = [url absoluteString];
    
    [self dismissViewControllerAnimated:true completion:nil];
}

@end
