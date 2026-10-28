# Quản lý danh bạ liên hệ

Bài thực hành Android của **Trần Hải Đăng — B24DCCC049**.

Ứng dụng dùng **Java + XML**, **4 Activity**, **Intent** và **ArrayList<Contact>** theo đề. Tài khoản thực hành: **admin / 123456**.

## Chức năng

- Đăng nhập: chặn ô rỗng, báo sai thông tin, đóng màn hình đăng nhập sau khi thành công.
- Danh sách: tên, điện thoại, nhóm, chữ đại diện, sao yêu thích và thống kê.
- Thêm: họ tên, số điện thoại, Spinner nhóm, RadioGroup giới tính, CheckBox yêu thích, ghi chú nhiều dòng.
- Kiểm tra tên bắt buộc; số khớp `0[0-9]{9}`; không chấp nhận trùng số.
- Chi tiết: đủ thông tin, thay đổi yêu thích, mở trình quay số bằng ACTION_DIAL, quay lại, xóa có xác nhận.
- Cập nhật danh sách/thống kê sau thêm, xóa và đổi yêu thích; xử lý danh sách rỗng và ID không còn tồn tại.
- Giữ biểu mẫu khi Activity tái tạo; xử lý insets thanh hệ thống và bàn phím.

## Chạy dự án

1. Clone repository và mở thư mục gốc bằng Android Studio.
2. Cài Android SDK API 37, đồng bộ Gradle. Dự án dùng AGP 9.3.3, Gradle Wrapper 9.5.0 và cấu hình JVM toolchain 25 trong `gradle/gradle-daemon-jvm.properties`.
3. Chọn thiết bị/emulator Android API 24 trở lên, Run module `app`.
4. Đăng nhập bằng tài khoản thực hành trên.

Dữ liệu **chỉ lưu trong bộ nhớ tiến trình**. Khi tiến trình bị kết thúc, lần chạy mới nạp lại ba liên hệ mẫu theo đề. Không có SQLite, Room, Firebase, API hoặc quyền đọc danh bạ điện thoại. Tài khoản cố định phục vụ bài thực hành.

## Kiểm thử

```powershell
.\gradlew.bat :app:testDebugUnitTest --tests com.example.myapplication.ContactRepositoryTest
.\gradlew.bat :app:assembleDebug :app:lintDebug
.\gradlew.bat :app:connectedDebugAndroidTest
```

Đã xác nhận **12 unit test** và **4 instrumentation test** đạt trên Pixel 4a, Android 17/API 37. Lint: **0 lỗi, 4 cảnh báo** (phiên bản AGP mới hơn, cấu hình backup và hai tài nguyên backup mặc định chưa dùng). Chi tiết: [Kết quả kiểm thử](docs/TEST_RESULTS.md).

APK debug sau build: `app/build/outputs/apk/debug/app-debug.apk`.

## Cấu trúc

| Thành phần | Vai trò |
| --- | --- |
| `Contact.java` | Các thuộc tính liên hệ và chữ đại diện |
| `ContactRepository.java` | ArrayList, dữ liệu mẫu, thêm/xóa/yêu thích, thống kê |
| `ContactsApplication.java` | Repository dùng chung trong tiến trình |
| `LoginValidator.java`, `ContactValidator.java` | Kiểm tra tài khoản và đầu vào |
| `LoginActivity.java` | Đăng nhập bằng tài khoản cố định |
| `MainActivity.java`, `ContactAdapter.java` | Danh sách và thống kê |
| `AddContactActivity.java` | Biểu mẫu thêm liên hệ |
| `ContactDetailActivity.java` | Nhận ID qua Intent, chi tiết, ACTION_DIAL, xóa |
| `BaseActivity.java` | Insets và xóa lỗi cũ khi sửa ô nhập |

## Báo cáo và minh chứng

- [Báo cáo PDF](B24DCCC049_TranHaiDang_BaoCao.pdf) gồm **9 trang, 12 hình minh chứng**, có ảnh và mô tả cho cả bốn màn hình; đã kiểm tra bố cục từng trang.
- [Ảnh chạy thật](docs/screenshots/) gồm đăng nhập, lỗi, biểu mẫu, danh sách sau cập nhật, chi tiết và trình quay số.
- [Bảng đối chiếu đề bài](docs/REQUIREMENTS.md).
- Dòng **Lớp** trong báo cáo chờ bổ sung thông tin người nộp.
- [Nguồn báo cáo](docs/report.html) có thể sửa nội dung, gồm dòng Lớp. Chạy `powershell -File tools/build-pdf.ps1` ở thư mục gốc để xuất lại PDF bằng Chrome/Edge; `powershell -File tools/render-pdf.ps1 -ProjectRoot (Get-Location).Path` kết xuất các trang ra PNG bằng Windows PDF để kiểm tra.
- Để chụp lại ảnh: chạy instrumentation test, rồi dùng `adb pull /sdcard/Download/quanlydanhba-evidence/. docs/screenshots`.

`.gitignore` loại `build`, `.gradle`, `.idea`, `local.properties` và ảnh kiểm tra bố cục báo cáo.
