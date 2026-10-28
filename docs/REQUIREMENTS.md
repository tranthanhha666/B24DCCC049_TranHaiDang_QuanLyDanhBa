# Đối chiếu đề bài Quản lý danh bạ

- Công nghệ bắt buộc: Java, giao diện XML, 4 Activity, Intent, ArrayList<Contact>.
- Dữ liệu chỉ lưu trong bộ nhớ của tiến trình; không dùng cơ sở dữ liệu, Firebase hoặc API.
- LoginActivity: EditText tên đăng nhập/mật khẩu, ẩn mật khẩu, kiểm tra rỗng, thông báo sai; tài khoản admin / 123456; Intent tới MainActivity và finish().
- MainActivity: ListView + Adapter với chữ cái đại diện, họ tên, điện thoại, nhóm, sao yêu thích; tổng số và số yêu thích; mở thêm/chi tiết bằng Intent.
- Dữ liệu mẫu: Nguyễn Văn An / 0901234567 / Bạn bè / không yêu thích; Trần Thị Bình / 0912345678 / Gia đình / yêu thích; Lê Minh Châu / 0987654321 / Công việc / không yêu thích.
- AddContactActivity: họ tên, điện thoại, Spinner (Gia đình, Bạn bè, Công việc, Khác), RadioGroup (Nam mặc định, Nữ, Khác), CheckBox yêu thích, ghi chú nhiều dòng, lưu.
- Không lưu tên rỗng; điện thoại phải khớp 0[0-9]{9}; chặn trùng số. Tạo Contact, quay lại và cập nhật danh sách/thống kê.
- ContactDetailActivity: đủ thông tin và chữ đại diện; thay đổi yêu thích; ACTION_DIAL; quay lại; xóa và cập nhật thống kê.
- Nộp toàn bộ mã nguồn Git, không ZIP; loại build/.gradle/.idea/local.properties.
- Báo cáo Word hoặc PDF: ảnh chụp đang chạy + mô tả cụ thể component/lớp/hàm/sự kiện cho cả 4 màn hình; chú thích rõ dưới ảnh.
- Báo cáo nên có ảnh lỗi đăng nhập, lỗi thêm, sau lưu và màn hình quay số.
- Tên báo cáo B24DCCC049_TranHaiDang_BaoCao.docx hoặc .pdf; đầu báo cáo ghi họ tên, MSSV, lớp, repository.
- Repository: https://github.com/tranthanhha666/B24DCCC049_TranHaiDang_QuanLyDanhBa
