package vn.edu.dlu.ctk47.techmate;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import kotlin.Unit;
import vn.edu.dlu.ctk47.techmate.model.Category;
import vn.edu.dlu.ctk47.techmate.model.Product;
import vn.edu.dlu.ctk47.techmate.firebase.ProductRepository;

public class HomeFragment extends Fragment {

    private ImageView btnCompareFloat;
    private RecyclerView rvCat, rvProd;
    private List<Product> productList = new ArrayList<>();
    private List<Category> categoryList = new ArrayList<>();
    private ProductAdapter productAdapter;
    private CategoryAdapter categoryAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btnCompareFloat = view.findViewById(R.id.btnCompareFloat);
        rvCat = view.findViewById(R.id.rvCategories);
        rvProd = view.findViewById(R.id.rvProducts);

        setupRecyclerViews(view);
        loadData();

        btnCompareFloat.setOnClickListener(v -> {
            if (CompareManager.get().size() < 2) {
                Toast.makeText(getContext(), "Chọn ít nhất 2 sản phẩm", Toast.LENGTH_SHORT).show();
                return;
            }
            Navigation.findNavController(view).navigate(R.id.compareFragment);
        });

        updateCompareUI();
    }

    private void setupRecyclerViews(View view) {
        rvCat.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        categoryAdapter = new CategoryAdapter(categoryList, category -> {
            if (category.getId() != null) {
                ProductRepository.INSTANCE.getProductsByCategory(category.getId(), products -> {
                    requireActivity().runOnUiThread(() -> {
                        productList.clear();
                        productList.addAll(products);
                        productAdapter.notifyDataSetChanged();
                    });
                    return Unit.INSTANCE;
                });
            }
        });
        rvCat.setAdapter(categoryAdapter);

        rvProd.setLayoutManager(new GridLayoutManager(getContext(), 2));
        productAdapter = new ProductAdapter(productList, product -> {
            if (CompareManager.hasOne()) {
                CompareManager.add(product);
                Navigation.findNavController(view).navigate(R.id.compareFragment);
                return;
            }
            Bundle bundle = new Bundle();
            bundle.putString("id", product.getId());
            Navigation.findNavController(view).navigate(R.id.detailFragment, bundle);
        });
        rvProd.setAdapter(productAdapter);
    }

    private void loadData() {
        ProductRepository.INSTANCE.getCategories(categories -> {
            if (isAdded() && getActivity() != null) {
                getActivity().runOnUiThread(() -> {
                    categoryList.clear();
                    categoryList.addAll(categories);
                    categoryAdapter.notifyDataSetChanged();
                });
            }
            return Unit.INSTANCE;
        });

        ProductRepository.INSTANCE.getProducts(products -> {
            if (isAdded() && getActivity() != null) {
                getActivity().runOnUiThread(() -> {
                    productList.clear();
                    productList.addAll(products);
                    productAdapter.notifyDataSetChanged();
                });
            }
            return Unit.INSTANCE;
        });
    }

    private void updateCompareUI() {
        int size = CompareManager.get().size();
        if (btnCompareFloat != null) {
            btnCompareFloat.setAlpha(size > 0 ? 1f : 0.4f);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        updateCompareUI();
    }
}
