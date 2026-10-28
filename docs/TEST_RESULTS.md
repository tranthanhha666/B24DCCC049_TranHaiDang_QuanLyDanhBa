# Kết quả kiểm thử

Ngày xác nhận: 28/10/2026.

## Môi trường

- Windows, Android Studio, module `:app`.
- Java và XML; compileSdk/targetSdk 37, minSdk 24.
- Pixel 4a emulator, Android 17/API 37, serial emulator-5554.
- Không yêu cầu quyền CALL_PHONE; chỉ dùng Intent.ACTION_DIAL.

## Kết quả đã chạy

| Kiểm tra | Kết quả |
| --- | --- |
| `:app:testDebugUnitTest` | 12/12 đạt, không bỏ qua |
| `:app:assembleDebug` | Thành công, tạo APK |
| `:app:lintDebug` | Thành công, 0 lỗi và 4 cảnh báo cấu hình/tài nguyên |
| `:app:connectedDebugAndroidTest` | 4/4 đạt trên emulator |

Các cảnh báo lint còn lại là thông báo có AGP mới hơn, hướng dẫn khai báo dataExtractionRules và hai XML backup mặc định chưa dùng. Ứng dụng không lưu dữ liệu danh bạ xuống tệp hoặc cơ sở dữ liệu.

## Nội dung unit test

ContactRepositoryTest kiểm tra 12 trường hợp: ba liên hệ mẫu và thống kê; định dạng điện thoại; tên bắt buộc; thêm đủ thuộc tính và chuẩn hóa khoảng trắng; chặn trùng số; chặn dữ liệu sai ngay tại repository; cập nhật yêu thích và xóa; ID không tồn tại; bản sao danh sách không làm thay đổi repository; ID không tái sử dụng sau xóa; chữ đại diện theo tên; tài khoản thực hành.

## Nội dung instrumentation test

1. **completeWorkflowAndCaptureReportScreenshots**: đăng nhập rỗng/sai/đúng; ba dữ liệu mẫu; Nam mặc định; thêm thiếu dữ liệu, sai số, trùng số; chọn nhóm/giới tính/yêu thích/ghi chú; lưu và kiểm tra 4/2; chi tiết đầy đủ; bỏ yêu thích và kiểm tra 4/1; hủy xóa; xác nhận xóa và kiểm tra 3/1; kiểm tra ACTION_DIAL + tel:0901234567 và chụp trình quay số thật.
2. **formRetainsInputAcrossActivityRecreation**: giữ tên, số điện thoại, giới tính, yêu thích và ghi chú sau ActivityScenario.recreate().
3. **invalidContactIdFinishesWithoutCrash**: ID không tồn tại kết thúc Activity an toàn.
4. **emptyListDisplaysHelpfulMessage**: danh sách rỗng, thông báo hướng dẫn và thống kê 0/0.

Ảnh gốc từ lần chạy thành công nằm trong `docs/screenshots`. Các số điện thoại là dữ liệu mẫu theo đề và dữ liệu kiểm thử; không thực hiện cuộc gọi.

## Giới hạn của xác nhận

Đã chạy trên một emulator API 37. Chưa chạy trên thiết bị vật lý hay toàn bộ các phiên bản Android từ API 24. Dữ liệu chỉ tồn tại trong tiến trình theo đúng đề, không phục hồi sau khi tiến trình bị kết thúc. Tài khoản hard-code phục vụ bài thực hành, không phải dịch vụ xác thực thực tế.
