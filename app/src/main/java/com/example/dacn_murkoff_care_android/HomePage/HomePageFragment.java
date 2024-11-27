    package com.example.dacn_murkoff_care_android.HomePage;

    import android.annotation.SuppressLint;
    import android.content.Context;
    import android.content.Intent;
    import android.os.Bundle;
    import android.view.LayoutInflater;
    import android.view.View;
    import android.view.ViewGroup;
    import android.widget.EditText;
    import android.widget.TextView;

    import androidx.annotation.NonNull;
    import androidx.fragment.app.Fragment;
    import androidx.lifecycle.ViewModelProvider;
    import androidx.recyclerview.widget.GridLayoutManager;
    import androidx.recyclerview.widget.LinearLayoutManager;
    import androidx.recyclerview.widget.RecyclerView;

    import com.example.dacn_murkoff_care_android.Configuration.Constant;
    import com.example.dacn_murkoff_care_android.Configuration.HTTPRequest;
    import com.example.dacn_murkoff_care_android.Configuration.HTTPService;
    import com.example.dacn_murkoff_care_android.Container.WeatherForecast;
    import com.example.dacn_murkoff_care_android.Helper.GlobalVariable;
    import com.example.dacn_murkoff_care_android.Helper.Tooltip;
    import com.example.dacn_murkoff_care_android.Model.Doctor;
    import com.example.dacn_murkoff_care_android.Model.Handbook;
    import com.example.dacn_murkoff_care_android.Model.Setting;
    import com.example.dacn_murkoff_care_android.Model.Speciality;
    import com.example.dacn_murkoff_care_android.R;
    import com.example.dacn_murkoff_care_android.RecyclerView.ButtonRecyclerView;
    import com.example.dacn_murkoff_care_android.RecyclerView.DoctorRecyclerView;
    import com.example.dacn_murkoff_care_android.RecyclerView.HandbookRecyclerView;
    import com.example.dacn_murkoff_care_android.RecyclerView.SpecialityRecyclerView;
    import com.example.dacn_murkoff_care_android.SearchPage.SearchPageActivity;

    import org.json.JSONObject;

    import java.util.ArrayList;
    import java.util.HashMap;
    import java.util.List;
    import java.util.Map;

    import retrofit2.Call;
    import retrofit2.Callback;
    import retrofit2.Response;
    import retrofit2.Retrofit;

    public class HomePageFragment extends Fragment {

        private final String TAG = "Home_Page_Fragment";

        private GlobalVariable globalVariable;

        private RecyclerView recyclerViewSpeciality;
        private RecyclerView recyclerViewDoctor;
        private RecyclerView recyclerViewHandbook;
        private RecyclerView recyclerViewRecommendedPages;


        private EditText searchBar;
        private TextView txtReadMoreSpeciality;
        private TextView txtReadMoreDoctor;

        private Context context;
        private RecyclerView recyclerViewButton;

        private TextView txtDate;
        private TextView txtWeather;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            // Inflate the layout for this fragment
            View view = inflater.inflate(R.layout.fragment_home_page, container, false);

            setupComponent(view);
            //setupViewModel();

            setupEvent();
            getCurrentWeather();

            setupRecyclerViewButton();
            setupRecyclerViewHandbook();
            setupRecyclerViewRecommendedPages();


            return view;
        }

        private void setupComponent(View view)
        {
            context = requireContext();
            globalVariable = (GlobalVariable) requireActivity().getApplication();

            recyclerViewSpeciality = view.findViewById(R.id.recyclerViewSpeciality);
            recyclerViewDoctor = view.findViewById(R.id.recyclerViewDoctor);
            recyclerViewButton = view.findViewById(R.id.recyclerViewButton);
            recyclerViewHandbook = view.findViewById(R.id.recyclerViewHandbook);
            recyclerViewRecommendedPages = view.findViewById(R.id.recyclerViewRecommendedPages);

            searchBar = view.findViewById(R.id.searchBar);
            txtReadMoreSpeciality = view.findViewById(R.id.txtReadMoreSpeciality);
            txtReadMoreDoctor = view.findViewById(R.id.txtReadMoreDoctor);

            txtWeather = view.findViewById(R.id.txtWeather);
            txtDate = view.findViewById(R.id.txtDate);

        }


        private void setupViewModel()
        {
            /*Step 1 - Khởi tạo ViewModel*/
            HomePageViewModel viewModel = new ViewModelProvider(this).get(HomePageViewModel.class);
            viewModel.instantiate();

            /*Step 2 - Chuẩn bị header & tham số*/
            Map<String, String> header = globalVariable.getHeaders();
            header.put("type", "patient");

            /*Step 3 - Listen Speciality Read All */
            Map<String, String> paramsSpeciality = new HashMap<>();
            viewModel.specialityReadAll(header, paramsSpeciality);

            viewModel.getSpecialityReadAllResponse().observe(getViewLifecycleOwner(), response->{
                int result = response.getResult();
                if( result == 1)
                {
                    List<Speciality> list = response.getData();
                    setupRecyclerViewSpeciality(list);
                }
            });


            /*Step 4 - Listen Doctor read all*/
            Map<String, String> paramsDoctor = new HashMap<>();
            viewModel.doctorReadAll(header, paramsDoctor);

    //        for (Map.Entry<String,String> entry : paramsDoctor.entrySet())
    //            System.out.println("Key = " + entry.getKey() +
    //                    ", Value = " + entry.getValue());
    //
    //        for (Map.Entry<String,String> entry : header.entrySet())
    //            System.out.println("Key = " + entry.getKey() +
    //                    ", Value = " + entry.getValue());

            viewModel.getDoctorReadAllResponse().observe(getViewLifecycleOwner(), response->{
                int result = response.getResult();
                if( result == 1)
                {
                    List<Doctor> list = response.getData();
                    setupRecyclerViewDoctor(list);
                }
            });
        }

        /** SETUP RECYCLER VIEW SPECIALITY **/
        private void setupRecyclerViewSpeciality(List<Speciality> list)
        {
            SpecialityRecyclerView specialityAdapter = new SpecialityRecyclerView(requireActivity(), list, R.layout.recycler_view_element_speciality);
            recyclerViewSpeciality.setAdapter(specialityAdapter);

            LinearLayoutManager manager = new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false);
            recyclerViewSpeciality.setLayoutManager(manager);
        }


        /** SETUP RECYCLER VIEW DOCTOR **/
        private void setupRecyclerViewDoctor(List<Doctor> list)
        {
            DoctorRecyclerView doctorAdapter = new DoctorRecyclerView(requireActivity(), list);
            recyclerViewDoctor.setAdapter(doctorAdapter);

            LinearLayoutManager manager = new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false);
            recyclerViewDoctor.setLayoutManager(manager);
        }


        /** SETUP EVENT FOR BUTTONS **/
        @SuppressLint({"UnspecifiedImmutableFlag", "ShortAlarm"})
        private void setupEvent()
        {
            /*SEARCH BAR*/
            searchBar.setOnClickListener(view->{
                Intent intent = new Intent(requireContext(), SearchPageActivity.class);
                startActivity(intent);
            });


            /*TXT READ MORE SPECIALITY*/
            txtReadMoreSpeciality.setOnClickListener(view->{
                Intent intent = new Intent(context, SearchPageActivity.class);
                String filterKey  = context.getString(R.string.speciality)  ;

                intent.putExtra("filterKey", filterKey );
                startActivity(intent);
            });

            /*TXT READ MORE DOCTOR*/
            txtReadMoreDoctor.setOnClickListener(view->{
                Intent intent = new Intent(context, SearchPageActivity.class);
                String filterKey  = context.getString(R.string.doctor);

                intent.putExtra("filterKey", filterKey );
                startActivity(intent);
            });
        }


        /** SETUP RECYCLER VIEW BUTTON **/
        private void setupRecyclerViewButton()
        {
            Setting setting0 = new Setting(R.drawable.ic_exam_speciality, "specialityExamination", getString(R.string.speciality_examination) );
            Setting setting1 = new Setting(R.drawable.ic_exam_general, "generalExamination", getString(R.string.general_examination) );
            Setting setting2 = new Setting(R.drawable.ic_exam_heart, "heartExamination", getString(R.string.heart_examination) );
            Setting setting3 = new Setting(R.drawable.ic_exam_pregnant, "pregnantExamination", getString(R.string.pregnant_examination) );
            Setting setting4 = new Setting(R.drawable.ic_exam_tooth, "toothExamination", context.getString(R.string.tooth_examination));
            Setting setting5 = new Setting(R.drawable.ic_exam_eye, "eyeExamination", getString(R.string.eye_examination) );
            Setting setting6 = new Setting(R.drawable.ic_exam_medical_test, "medicalTestExamination", context.getString(R.string.medical_test_examination));

            List<Setting> list = new ArrayList<>();
            list.add(setting0);
            list.add(setting1);
            list.add(setting2);
            list.add(setting3);
            list.add(setting4);
            list.add(setting5);
            list.add(setting6);

            ButtonRecyclerView buttonAdapter = new ButtonRecyclerView(requireActivity(), list);
            recyclerViewButton.setAdapter(buttonAdapter);

            GridLayoutManager manager = new GridLayoutManager(requireContext(), 3);
            recyclerViewButton.setLayoutManager(manager);
        }


        /** SETUP RECYCLER VIEW HANDBOOK **/
        private void setupRecyclerViewHandbook()
        {
            Handbook handbook0 = new Handbook(
                    "https://vignette.wikia.nocookie.net/outlast/images/c/cc/Murkoff_Corp_Logo.jpg/revision/latest?cb=20140308195505",
                    "MurkOff Care - Sức khoẻ của bạn, sứ mệnh của chúng tôi",
                    "https://www.youtube.com/watch?v=RGNRtwCaY4g");

            Handbook handbook1 = new Handbook(
                    "https://images2.thanhnien.vn/thumb_w/640/528068263637045248/2024/11/17/xn-2-tuan-minh-17318346455671393311874.jpg",
                    "Bệnh viện công áp dụng giá mới cho khám chữa bệnh bảo hiểm y tế",
                    "https://thanhnien.vn/benh-vien-cong-ap-dung-gia-moi-cho-kham-chua-benh-bao-hiem-y-te-185241117162343931.htm");

            Handbook handbook2 = new Handbook(
                    "https://i1-suckhoe.vnecdn.net/2024/11/18/233A1567-4275-1731905836.jpg?w=1020&h=0&q=100&dpr=1&fit=crop&s=Pes7J8mDfPEGehTwAZQt7g",
                    "Tại sao dễ đột quỵ vào thời điểm cuối năm?",
                    "https://vnexpress.net/tai-sao-de-dot-quy-vao-thoi-diem-cuoi-nam-4817275.html");

            Handbook handbook3 = new Handbook(
                    "https://i1-suckhoe.vnecdn.net/2024/11/15/z4285932162743-e56923506ecf13b-5026-2003-1731640663.jpg?w=1020&h=0&q=100&dpr=1&fit=crop&s=jQlZKJSfKDkjXyUlW7gl5Q",
                    "Nhồi máu cơ tim cấp tấn công người trẻ",
                    "https://vnexpress.net/nhoi-mau-co-tim-cap-tan-cong-nguoi-tre-4814522.html");

            Handbook handbook4 = new Handbook(
                    "https://cdn.tuoitre.vn/thumb_w/730/471584752817336320/2024/11/18/ai-17319073299211524683030.png",
                    "Ngồi nhiều, nam thanh niên mắc hội chứng cơ hình lê",
                    "https://tuoitre.vn/ngoi-nhieu-nam-thanh-nien-mac-hoi-chung-co-hinh-le-20241118122609791.htm");

            Handbook handbook5 = new Handbook(
                    "https://suckhoedoisong.qltns.mediacdn.vn/thumb_w/640/324455921873985536/2024/11/17/edit-sot-cao-uong-thuoc-phong-kham-tu-khong-giam-1731805598859804274282.jpeg",
                    "Sốt cao mãi không khỏi, đi khám phát hiện mắc Whitmore chưa rõ nguồn lây",
                    "https://suckhoedoisong.vn/sot-cao-mai-khong-khoi-di-kham-phat-hien-mac-whitmore-chua-ro-nguon-lay-169241117081249257.htm");

            Handbook handbook6 = new Handbook(
                    "https://suckhoedoisong.qltns.mediacdn.vn/thumb_w/640/324455921873985536/2024/11/12/1-1731415007467943864981.jpg",
                    "Tăng nguy cơ vô sinh nếu trẻ vị thành niên mắc các bệnh lây truyền qua đường tình dục, trong đó có HIV",
                    "https://suckhoedoisong.vn/tang-nguy-co-vo-sinh-neu-tre-vi-thanh-nien-mac-cac-benh-lay-truyen-qua-duong-tinh-duc-trong-do-co-hiv-169241112193938803.htm");

            Handbook handbook7 = new Handbook(
                    "https://i1-suckhoe.vnecdn.net/2024/11/18/ungthuvu-1228-1731888748.png?w=1020&h=0&q=100&dpr=1&fit=crop&s=mJqlVSPrErK11JPZ86uDLQ",
                    "Điều gì xảy ra khi bạn bị stress?",
                    "https://vnexpress.net/dieu-gi-xay-ra-khi-ban-bi-stress-4817160.html");

            Handbook handbook8 = new Handbook(
                    "https://i1-suckhoe.vnecdn.net/2024/10/30/am-ho-1160-1730258075.png?w=1020&h=0&q=100&dpr=1&fit=crop&s=Ezs0sPvhNS7tTumjZHa8wg",
                    "Triệt căn ung thư cho cụ bà 73 tuổi",
                    "https://vnexpress.net/triet-can-ung-thu-cho-cu-ba-73-tuoi-4810119.html");

            Handbook handbook9 = new Handbook(
                    "https://images2.thanhnien.vn/thumb_w/640/528068263637045248/2024/11/16/kham-benh-tai-benh-vien-hung-vuong-quan-5-tphcm-anh-nhat-thinh-7-17317583435611550010709.jpg",
                    "Bác sĩ giỏi phải dành ít nhất 70% thời gian cho bệnh nhân BHYT",
                    "https://thanhnien.vn/bac-si-gioi-phai-danh-it-nhat-70-thoi-gian-cho-benh-nhan-bhyt-185241116190219125.htm");

            Handbook handbook10 = new Handbook(
                    "https://nld.mediacdn.vn/thumb_w/640/291774122806476800/2024/11/15/15-hinh-chot-15-11-1731677659332748597721.jpg",
                    "Tăng cường phòng, chống dịch sởi lây lan",
                    "https://nld.com.vn/tang-cuong-phong-chong-dich-soi-lay-lan-196241115203502882.htm");

            Handbook handbook11 = new Handbook(
                    "https://nld.mediacdn.vn/thumb_w/640/291774122806476800/2024/11/15/anh-man-hinh-2024-11-15-luc-093907-1731638401853174624035.png",
                    "Một phòng khám thẩm mỹ 3 lần đổi tên bất chấp vi phạm",
                    "https://nld.com.vn/mot-phong-kham-tham-my-3-lan-doi-ten-bat-chap-vi-pham-196241115094351926.htm");

            Handbook handbook12 = new Handbook(
                    "https://cdnphoto.dantri.com.vn/RvmH8ElBz2hX8bD-m3UvYJ_fhU8=/thumb_w/1020/2024/11/16/screen-shot-2023-12-29-at-45745-pm-edited-1703843900338-crop-1731756015274.jpeg",
                    "\"Đại dịch\" đái tháo đường trẻ hóa, BHYT Hà Nội chi 1.000 tỷ tiền điều trị",
                    "https://dantri.com.vn/suc-khoe/dai-dich-dai-thao-duong-tre-hoa-bhyt-ha-noi-chi-1000-ty-tien-dieu-tri-20241116182316360.htm");

            Handbook handbook13 = new Handbook(
                    "https://cdnphoto.dantri.com.vn/p4uxx9r-OPsBWTKMFajxRWh8i1E=/thumb_w/1020/2024/11/13/vet-bam-tim-edited-1731485735212.jpeg",
                    "Vết bầm tím chỉ điểm những loại ung thư nguy hiểm",
                    "https://dantri.com.vn/suc-khoe/vet-bam-tim-chi-diem-nhung-loai-ung-thu-nguy-hiem-20241113152735179.htm");

            Handbook handbook14 = new Handbook(
                    "https://image.tienphong.vn/w645/Uploaded/2024/zaugtn/2024_11_16/a1-3804.jpeg",
                    "Bệnh viện Thẩm mỹ Sao Hàn: Đầu tư nâng cấp toàn diện cơ sở vật chất",
                    "https://tienphong.vn/benh-vien-tham-my-sao-han-dau-tu-nang-cap-toan-dien-co-so-vat-chat-post1692092.tpo");

            Handbook handbook15 = new Handbook(
                    "https://cdn.tuoitre.vn/thumb_w/1200/471584752817336320/2024/6/5/25-3-ho-tieu-duoc-gia-nong-dan-van-chua-vui-5-17175601969981915774124.jpg",
                    "Hạt tiêu, loại gia vị có nhiều công dụng bất ngờ",
                    "https://tuoitre.vn/hat-tieu-loai-gia-vi-co-nhieu-cong-dung-bat-ngo-20241116142604743.htm");

            Handbook handbook16 = new Handbook(
                    "https://static.tuoitre.vn/tto/i/s626/2014/10/13/ahB8E5pI.jpg",
                    "4 gia vị mùa thu tốt cho sức khỏe bạn",
                    "https://tuoitre.vn/bon-gia-vi-mua-thu-tot-cho-suc-khoe-ban-657640.htm");

            Handbook handbook17 = new Handbook(
                    "https://i1-suckhoe.vnecdn.net/2024/10/30/pexels-dana-tentis-118658-1213-8379-6569-1730228514.jpg?w=1020&h=0&q=100&dpr=1&fit=crop&s=CKsxASvTWA60pAVZT3COhw",
                    "Chế độ ăn uống giảm nguy cơ ung thư",
                    "https://vnexpress.net/che-do-an-uong-giam-nguy-co-ung-thu-4809972.html");

            List<Handbook> list = new ArrayList<>();
            list.add(handbook0);
            list.add(handbook1);
            list.add(handbook2);
            list.add(handbook3);
            list.add(handbook4);
            list.add(handbook5);
            list.add(handbook6);
            list.add(handbook7);
            list.add(handbook8);
            list.add(handbook9);
            list.add(handbook10);
            list.add(handbook11);
            list.add(handbook12);
            list.add(handbook13);
            list.add(handbook14);
            list.add(handbook15);
            list.add(handbook16);
            list.add(handbook17);

            HandbookRecyclerView handbookAdapter = new HandbookRecyclerView(requireActivity(), list, R.layout.recycler_view_element_handbook);
            recyclerViewHandbook.setAdapter(handbookAdapter);

            LinearLayoutManager manager = new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false);
            recyclerViewHandbook.setLayoutManager(manager);
        }



        /** SETUP RECYCLER VIEW RECOMMENDED PAGES **/
        private void setupRecyclerViewRecommendedPages()
        {
            Handbook handbook0 = new Handbook(
                    "https://suckhoedoisong.qltns.mediacdn.vn/thumb_w/1200/324455921873985536/2021/10/10/baoskds-1633861575912643818497-49-0-295-393-crop-1633861583124643700229.png",
                    "Sức Khỏe & Đời Sống - Tin nóng y tế",
                    "https://suckhoedoisong.vn/y-te/tin-nong-y-te.htm");

            Handbook handbook1 = new Handbook(
                    "https://th.bing.com/th/id/OIP.20TSC_njluy2XohkaiHQ5QHaD3?w=320&h=180&c=7&r=0&o=5&dpr=1.3&pid=1.7",
                    "Báo Tuổi Trẻ - Sức khoẻ dinh dưỡng",
                    "https://tuoitre.vn/suc-khoe/dinh-duong.htm");

            Handbook handbook2 = new Handbook(
                    "https://images.squarespace-cdn.com/content/v1/61d0e8c0798e1541df27179c/023eb4fe-9dd5-4d39-a314-9d237587f679/thanhnien-vn-favicon-51707a2e.jpg",
                    "Báo Thanh Niên - Sức khoẻ, khoẻ đẹp mỗi ngày",
                    "https://thanhnien.vn/suc-khoe/khoe-dep-moi-ngay.htm");

            Handbook handbook3 = new Handbook(
                    "https://th.bing.com/th/id/OIP.dDBMrnvCAkda3EiU-nR74gHaCt?w=302&h=128&c=7&r=0&o=5&dpr=1.3&pid=1.7",
                    "Cổng thông tin Bộ Y Tế - Hoạt động Lãnh đạo Bộ",
                    "https://moh.gov.vn/hoat-dong-cua-lanh-dao-bo");


            List<Handbook> list = new ArrayList<>();
            list.add(handbook0);
            list.add(handbook1);
            list.add(handbook2);
            list.add(handbook3);

            HandbookRecyclerView handbookAdapter = new HandbookRecyclerView(requireActivity(), list, R.layout.recycler_view_element_handbook_2);
            recyclerViewRecommendedPages.setAdapter(handbookAdapter);

            LinearLayoutManager manager = new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false);
            recyclerViewRecommendedPages.setLayoutManager(manager);
        }




        /** SETUP "openweathermap" API **/
        private void getCurrentWeather()
        {
            Retrofit service = HTTPService.getOpenWeatherMapInstance();
            HTTPRequest api = service.create(HTTPRequest.class);

            /*Step 3*/
            Map<String, String> parameters = new HashMap<>();
            String latitude = "10.848160";// kinh độ TP.Thủ Đức
            String longitude = "106.772522";// vĩ độ TP.Thủ Đức
            String apiKey = Constant.OPEN_WEATHER_MAP_API_KEY();// api key trên open weather map.org

            parameters.put("lat", latitude);
            parameters.put("lon", longitude);
            parameters.put("appid", apiKey);
            Call<WeatherForecast> container = api.getCurrentWeather(parameters);

            /*Step 4*/
            container.enqueue(new Callback<WeatherForecast>() {
                @Override
                public void onResponse(@NonNull Call<WeatherForecast> call, @NonNull Response<WeatherForecast> response) {
                    if(response.isSuccessful())
                    {
                        WeatherForecast content = response.body();
                        assert content != null;
    //                    System.out.println(TAG);
    //                    System.out.println("timezone: " + content.getTimeZone());
    //                    System.out.println("name: " + content.getName());
                        printDateAndWeather(content);

                    }
                    if(response.errorBody() != null)
                    {
                        try
                        {
                            JSONObject jObjError = new JSONObject(response.errorBody().string());
                            System.out.println( jObjError );
                        }
                        catch (Exception e) {
                            System.out.println( e.getMessage() );
                        }
                    }
                }

                @Override
                public void onFailure(@NonNull Call<WeatherForecast> call, @NonNull Throwable t) {
                    System.out.println(TAG);
                    System.out.println("Error: " + t.getMessage());
                }
            });
        }

        /** PRINT WEATHER ON SCREEN **/
        private void printDateAndWeather(WeatherForecast content)
        {
            String today = Tooltip.getReadableToday(context);
            txtDate.setText(today);

            String temperature = String.valueOf(content.getMain().getTemp());
            if (context != null && isAdded()) {
                String weatherInfo = temperature.substring(0, 2) + getString(R.string.celsius);
                txtWeather.setText(weatherInfo);
            }
        }
    }