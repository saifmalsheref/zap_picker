# zap_picker Flutter Package

The `zap_picker` package is a versatile Flutter package designed to simplify media and file picking in your Flutter applications. With `zap_picker`, you can effortlessly integrate functionality to pick media, including images and videos, from both the gallery and video apps installed on the user's system.

## Table of Contents

1. [pickMedia](#pickmedia)
   1. [Overview](#overview)
   1. [Adding Permissions](#adding-permissions)
   2. [Properties](#properties)
   3. [Example](#example)

2. [pickFile](#pickfile)
   1. [Overview](#overview-1)
   2. [Properties](#properties-1)
   3. [Example](#example-1)

---

## pickMedia

### Overview

The `pickMedia` function allows you to seamlessly pick images or videos based on your application's requirements. It utilizes the `MethodChannel` to communicate with the native platform, ensuring a smooth media picking experience. The function returns the file path of the picked media.


### Adding Permissions

To ensure proper functionality of the `zap_picker` package on Android, it is essential to include the necessary permissions for reading and writing external storage in your `AndroidManifest.xml` file.
Add the following lines within the `<manifest>` element of your `AndroidManifest.xml` file:

```xml
<uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
```


### Properties

- **PickerType pickerType** The type of picker to be used ( Gallery, File )

- **MediaType mediaType** The type of media to be picked  ( Image, Video )


### Example

```dart
import 'package:zap_picker/zap_picker.dart';

// Example: Pick an image from the gallery
String? imagePath = await ZapPicker.pickMedia(
  pickerType: PickerType.Gallery,
  mediaType: MediaType.Image,
);
```

------

## pickFile

### Overview

The `pickFile` function enables you to pick a file based on the specified file type and optional custom type. Similar to pickMedia, it utilizes the MethodChannel to communicate with the native platform, providing a consistent file picking experience. The function returns the file path of the picked file.

### Properties

- **FileType fileType** The specific type of file to be picked (PDF, Image, AnyType, Video, OtherType)

- **customType**  (Optional) List<String> of custom file types if you selected *fileType.OtherType* to be picked


### Example

```dart
import 'package:zap_picker/zap_picker.dart';

// Example: Pick a PDF file
String? pdfPath = await ZapPicker.pickFile(
  fileType: FileType.PDF,
);

// Example: Pick a custom file type (e.g., TXT, DOC)
String? customFilePath = await ZapPicker.pickFile(
  fileType: FileType.OtherType,
  customType: ['txt', 'doc'],
);

```