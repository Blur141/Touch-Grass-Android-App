# Add project specific ProGuard rules here.
-keepattributes *Annotation*
-keepattributes EnclosingMethod
-keepattributes InnerClasses

# Hilt
-keep class dagger.hilt.** { *; }
-keep class javax.inject.** { *; }

# Room
-keep class * extends androidx.room.RoomDatabase
-keep @androidx.room.Entity class *

# Keep data classes used in Room
-keepclassmembers class * {
    @androidx.room.* <methods>;
}
