package com.example.prm392project.presentation.login;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.prm392project.R;
import com.example.prm392project.common.api.ApiService;
import com.example.prm392project.databinding.FragmentRegisterBinding;
import com.example.prm392project.model.User;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RegisterFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RegisterFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private FragmentRegisterBinding binding;

    public RegisterFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentRegisterBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        EditText username = binding.idEdtUserName;
        EditText password = binding.idEdtPassword;
        EditText confirmpassword = binding.idEdtPasswordConfirm;

        binding.idBtnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = username.getText().toString();
                String p = password.getText().toString();
                String p2 = confirmpassword.getText().toString();
                if (p != null && p2 != null && p.equals(p2)) {

                    User userRegister = new User(name, p);

                    ApiService.apiService.register(userRegister)
                            .enqueue(new Callback<Void>() {
                                @Override
                                public void onResponse(Call<Void> call, Response<Void> response) {
                                    if (response.code() == 200) {
                                        Toast.makeText(requireContext(), "Đăng ký thành công", Toast.LENGTH_SHORT).show();
                                        Fragment loginFragment = new LoginFragment();
                                        FragmentTransaction transaction = getFragmentManager().beginTransaction();
                                        transaction.replace(R.id.fragment_register_id , loginFragment );
                                        transaction.addToBackStack(null); // Để có khả năng quay lại fragment login
                                        transaction.commit();
                                    } else if(response.code() == 409){
                                        Toast.makeText(requireContext(), "Tên đăng nhập đã tồn tại", Toast.LENGTH_SHORT).show();
                                    }
                                }
                                @Override
                                public void onFailure(Call<Void> call, Throwable t) {

                                }
                            });
                } else {
                    Toast.makeText(requireContext(), "Mật khẩu confirm không khớp", Toast.LENGTH_SHORT).show();
                }
            }
        });


        binding.btnGoToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment loginFragment = new LoginFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_register_id, loginFragment);
                transaction.addToBackStack(null); // Để có khả năng quay lại fragment login
                transaction.commit();

            }
        });

    }


    private void addUserByAPI() {

    }
}