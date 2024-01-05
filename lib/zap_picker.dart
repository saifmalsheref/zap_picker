library zap_picker;

import 'package:flutter/services.dart';
// ignore_for_file: constant_identifier_names, avoid_print

/// Enum representing the type of media to be picked, either [Image] or [Video].
enum MediaType { Image, Video }

/// Enum representing the type of picker to be used, either [Gallery] or [File].
enum PickerType { Gallery, File }

/// Enum representing the specific file type to be picked.
enum FileType { PDF, Image, AnyType, Video, OtherType }

/// A utility class for picking media and files using platform channels.
class ZapPicker {
  /// Picks media based on the specified [pickerType] and [mediaType].
  ///
  /// Returns the file path of the picked media.
  ///
  /// Throws a [PlatformException] if the operation fails.
  static Future<String?> pickMedia({
    required PickerType pickerType,
    required MediaType mediaType,
  }) async {
    const MethodChannel channel = MethodChannel('MediaPicker');

    try {
      var filePath = await channel.invokeMethod('pickMedia', {
        'mediaType': mediaType.toString().split('.').last,
        'pickerType': pickerType.toString().split('.').last,
      });

      return filePath;
    } catch (e) {
      // Handle platform-specific exceptions if needed
      print('Error picking media: $e');
      rethrow;
    }
  }

  /// Picks a file based on the specified [fileType] and optional [customType].
  ///
  /// Returns the file path of the picked file.
  ///
  /// Throws a [PlatformException] if the operation fails.
  static Future<String?> pickFile({
    required FileType fileType,
    List<String>? customType,
  }) async {
    const MethodChannel channel = MethodChannel('MediaPicker');

    try {
      var filePath = await channel.invokeMethod('pickMedia', {
        'mediaType': "File",
        'pickerType': fileType.toString().split('.').last,
        'options': {"fileTypes": customType},
      });

      return filePath;
    } catch (e) {
      // Handle platform-specific exceptions if needed
      print('Error picking file: $e');
      rethrow;
    }
  }
}
