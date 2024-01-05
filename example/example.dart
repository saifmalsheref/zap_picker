import 'package:flutter/material.dart';
import 'package:zap_picker/zap_picker.dart';
void main(List<String> args) {
  runApp(const MyWidget());
}

class MyWidget extends StatelessWidget {
  const MyWidget({super.key});

  @override
  Widget build(BuildContext context) {
    return const Placeholder();
  }
}

class ZapPickerWidget extends StatelessWidget {
  const ZapPickerWidget({super.key,});
  @override
  Widget build(BuildContext context) {
    return Scaffold(
        appBar: AppBar(
          title: const Text("Zap Picker Example"),
        ),
        body: Center(
          child: Column(
            mainAxisAlignment: MainAxisAlignment.center,
            children: [
              ElevatedButton(
                onPressed: () {
                  // Handle button press for picking an image from the gallery
                  _handleMediaPick(
                      context, PickerType.Gallery, MediaType.Image);
                },
                child: const Text('Pick Image from Gallery'),
              ),
              const SizedBox(height: 20),
              ElevatedButton(
                onPressed: () {
                  // Handle button press for picking a video from the gallery
                  _handleMediaPick(
                      context, PickerType.Gallery, MediaType.Video);
                },
                child: const Text('Pick Video from Gallery'),
              ),
              const SizedBox(height: 20),
              ElevatedButton(
                onPressed: () {
                  // Handle button press for picking any type of file
                  _handleFilePick(context, FileType.AnyType);
                },
                child: const Text('Pick Any Type of File'),
              ),
              const SizedBox(height: 20),
              ElevatedButton(
                onPressed: () {
                  // Handle button press for picking a PDF file
                  _handleFilePick(context, FileType.PDF);
                },
                child: const Text('Pick PDF File'),
              ),
              const SizedBox(height: 20),
              ElevatedButton(
                onPressed: () {
                  // Handle button press for picking a custom file type
                  _handleFilePick(context, FileType.OtherType,
                      customTypes:['image/*', 'pdf/*','video/*']);
                },
                child: const Text('Pick Custom File Type'),
              ),
            ],
          ),
        ));
  }

  Widget buildOptionButton(String label, void Function()? onPressed) {
    return ElevatedButton(
      onPressed: onPressed,
      child: Text(label),
    );
  }
}

// Helper method for handling media pick
void _handleMediaPick(
    BuildContext context, PickerType pickerType, MediaType mediaType) async {
  try {
    String? filePath = await ZapPicker.pickMedia(
        pickerType: pickerType, mediaType: mediaType);
    if (filePath != null) {
      // Handle the selected media file path
      print('Picked media file path: $filePath');
      // Perform additional actions as needed
    }
  } catch (e) {
    // Handle exceptions
    print('Error picking media: $e');
    // Show error message to the user if needed
  }
}

// Helper method for handling file pick
void _handleFilePick(BuildContext context, FileType fileType,
    {List<String>? customTypes}) async {
  try {
    var filePath =
        await ZapPicker.pickFile(fileType: fileType, customType: customTypes);
    if (filePath != null) {
      // Handle the selected file path
      print('Picked file path: $filePath');
      // Perform additional actions as needed
    }
  } catch (e) {
    // Handle exceptions
    print('Error picking file: $e');
    // Show error message to the user if needed
  }
}