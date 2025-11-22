package com.mobdeve.s17.group11.smartspend.expenses;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.mobdeve.s17.group11.smartspend.R;
import com.mobdeve.s17.group11.smartspend.budgets.BudgetsDatabase;
import com.mobdeve.s17.group11.smartspend.budgets.BudgetsListItem;
import com.mobdeve.s17.group11.smartspend.gallery.GalleryActivity;
import com.mobdeve.s17.group11.smartspend.gallery.GalleryImageViewerActivity;
import com.mobdeve.s17.group11.smartspend.gallery.GalleryUtils;
import com.mobdeve.s17.group11.smartspend.util.Date;
import com.mobdeve.s17.group11.smartspend.util.DropdownComposite;
import com.mobdeve.s17.group11.smartspend.util.NavigationBar;
import com.mobdeve.s17.group11.smartspend.util.SessionCache;
import com.mobdeve.s17.group11.smartspend.util.UIUtils;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

@SuppressLint("SetTextI18n")
public class ExpensesEditActivity extends AppCompatActivity {

    public static ExpensesListItem expenseEdit;
    public static Runnable exitListener;
    public static WeakReference<RecyclerView> rvExpensesListRef;

    private Button btnSave;
    private DropdownComposite categoryDropdownComposite = new DropdownComposite();
    private EditText tfAmount;
    private EditText tfCategory;
    private EditText tfDateDay, tfDateMonth, tfDateYear;
    private EditText tfLocation;
    private EditText tfNotes;
    private File tempImageFile = null;
    private GalleryUtils.Camera camera;
    private GalleryUtils.GalleryPicker galleryPicker;
    private ImageButton btnBack;
    private ImageButton btnDateCalendar;
    private ImageView imgThumbnail;
    private LinearLayout llAddImage;
    private LinearLayout llDeleteImage;
    private Set<BudgetsListItem> presentInBudgets = new HashSet<>();
    private TextView tvAddImage;
    private TextView tvAmountPrompt;
    private TextView tvCategoryPrompt;
    private TextView tvDatePrompt;
    private TextView tvDelete;
    private boolean deletedImage = false;
    private boolean hasImagePreview = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_expenses_edit);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.cl_root), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        NavigationBar.init(this);

        initNativeActivities();
        initViews();
        initListeners();
        initRecyclerViews();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if(tempImageFile != null && tempImageFile.exists())
            tempImageFile.delete();
    }

    private void initNativeActivities() {
        camera = new GalleryUtils.Camera(this, new GalleryUtils.Camera.Callback() {

            @Override
            public void onPhotoCaptured(File file) {
                tempImageFile = file;

                imgThumbnail.setImageTintList(ColorStateList.valueOf(0));

                Glide.with(ExpensesEditActivity.this)
                        .load(file)
                        .override(256, 256)
                        .centerCrop()
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(true)
                        .error(R.drawable.ic_image_missing)
                        .into(imgThumbnail);

                tvAddImage.setText("Edit");
                llDeleteImage.setVisibility(LinearLayout.VISIBLE);

                deletedImage = false;
                hasImagePreview = true;
            }

            @Override
            public void onCancelled() {}

        });

        galleryPicker = new GalleryUtils.GalleryPicker(this, new GalleryUtils.GalleryPicker.Callback() {

            @Override
            public void onImagePicked(File file) {
                tempImageFile = file;

                imgThumbnail.setImageTintList(ColorStateList.valueOf(0));

                Glide.with(ExpensesEditActivity.this)
                        .load(file)
                        .override(256, 256)
                        .centerCrop()
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(true)
                        .error(R.drawable.ic_image_missing)
                        .into(imgThumbnail);

                tvAddImage.setText("Edit");
                llDeleteImage.setVisibility(LinearLayout.VISIBLE);

                deletedImage = false;
                hasImagePreview = true;
            }

            @Override
            public void onCancelled() {}

        });
    }

    private void initViews() {
        btnBack = findViewById(R.id.btn_header_back);
        btnDateCalendar = findViewById(R.id.btn_date_calendar);
        btnSave = findViewById(R.id.btn_save);
        imgThumbnail = findViewById(R.id.img_thumbnail);
        llAddImage = findViewById(R.id.ll_image_add);
        llDeleteImage = findViewById(R.id.ll_image_delete);
        tfAmount = findViewById(R.id.tf_amount);
        tfCategory = findViewById(R.id.tf_category);
        tfDateDay = findViewById(R.id.tf_date_day);
        tfDateMonth = findViewById(R.id.tf_date_month);
        tfDateYear = findViewById(R.id.tf_date_year);
        tfLocation = findViewById(R.id.tf_location);
        tfNotes = findViewById(R.id.tf_notes);
        tvAddImage = findViewById(R.id.tv_image_add);
        tvAmountPrompt = findViewById(R.id.tv_amount_prompt);
        tvCategoryPrompt = findViewById(R.id.tv_category_prompt);
        tvDatePrompt = findViewById(R.id.tv_date_prompt);
        tvDelete = findViewById(R.id.tv_delete);

        tfAmount.setText(Float.toString(expenseEdit.amount));
        tfCategory.setText(ExpensesCategory.getExpensesCategoryName(expenseEdit.expensesCategoryID));
        tfDateDay.setText(Integer.toString(expenseEdit.date.day));
        tfDateMonth.setText(Integer.toString(expenseEdit.date.month));
        tfDateYear.setText(Integer.toString(expenseEdit.date.year));
        tfLocation.setText(expenseEdit.location);
        tfNotes.setText(expenseEdit.notes);

        File imagePreviewFile = new File(SessionCache.galleryDirectory, expenseEdit.sqlRowID + ".jpg");

        if(imagePreviewFile.exists()) {
            imgThumbnail.setImageTintList(ColorStateList.valueOf(0));

            Glide.with(this)
                    .load(imagePreviewFile)
                    .override(256, 256)
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .error(R.drawable.ic_image_missing)
                    .into(imgThumbnail);

            tvAddImage.setText("Edit");
            llDeleteImage.setVisibility(LinearLayout.VISIBLE);

            hasImagePreview = true;
        } else {
            tvAddImage.setText("Add");
            llDeleteImage.setVisibility(LinearLayout.GONE);

            hasImagePreview = false;
        }
    }

    private void initListeners() {
        ExpensesListAdapter expensesListAdapter = (ExpensesListAdapter) rvExpensesListRef.get().getAdapter();
        assert expensesListAdapter != null;

        btnBack.setOnClickListener(view -> {
            finish();
            overridePendingTransition(0, 0);
        });

        btnDateCalendar.setOnClickListener(view -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    ExpensesEditActivity.this,

                    (DatePicker datePicker, int selectedYear, int selectedMonth, int selectedDay) -> {
                        tfDateDay.setText(Integer.toString(selectedDay));
                        tfDateMonth.setText(Integer.toString(selectedMonth + 1));
                        tfDateYear.setText(Integer.toString(selectedYear));
                    },

                    Integer.parseInt(tfDateYear.getText().toString().trim()),
                    Integer.parseInt(tfDateMonth.getText().toString().trim()) - 1,
                    Integer.parseInt(tfDateDay.getText().toString().trim())
            );

            datePickerDialog.show();
        });

        btnSave.setOnClickListener(view -> {
            boolean validFields;

            validFields = UIUtils.Validator.validateAmountField(tfAmount, tvAmountPrompt);
            validFields &= UIUtils.Validator.validateCategoryField(tfCategory, tvCategoryPrompt);
            validFields &= UIUtils.Validator.validateDateFields(tfDateDay, tfDateMonth, tfDateYear, tvDatePrompt);

            if(!validFields)
                return;

            presentInBudgets.clear();

            SessionCache.budgetsItems.forEach(budget -> {
                int budgetDateEnd = budget.endDate.getUniqueValue();
                int budgetDateStart = budget.startDate.getUniqueValue();
                int expenseDate = expenseEdit.date.getUniqueValue();

                if(budget.expensesCategoryID == expenseEdit.expensesCategoryID
                        && budgetDateStart <= expenseDate
                        && budgetDateEnd >= expenseDate) {
                    presentInBudgets.add(budget);
                }
            });

            float lastAmount = expenseEdit.amount;

            expenseEdit.amount = Float.parseFloat(tfAmount.getText().toString().trim());

            expenseEdit.date = new Date(
                    Integer.parseInt(tfDateDay.getText().toString().trim()),
                    Integer.parseInt(tfDateMonth.getText().toString().trim()),
                    Integer.parseInt(tfDateYear.getText().toString().trim())
            );

            expenseEdit.expensesCategoryID = ExpensesCategory.getExpensesCategoryID(tfCategory.getText().toString().trim());
            expenseEdit.location = tfLocation.getText().toString().trim();
            expenseEdit.notes = tfNotes.getText().toString().trim();

            AtomicBoolean budgetExceeded = new AtomicBoolean(false);

            SessionCache.budgetsItems.forEach(budget -> {
                boolean updateBudgetsDB = false;

                int budgetDateEnd = budget.endDate.getUniqueValue();
                int budgetDateStart = budget.startDate.getUniqueValue();
                int expenseDate = expenseEdit.date.getUniqueValue();

                if(budget.expensesCategoryID == expenseEdit.expensesCategoryID
                        && budgetDateStart <= expenseDate
                        && budgetDateEnd >= expenseDate) {
                    if(presentInBudgets.contains(budget))
                        budget.currentAmount -= lastAmount - expenseEdit.amount;
                    else
                        budget.currentAmount += expenseEdit.amount;

                    if(budget.maxAmount < budget.currentAmount)
                        budgetExceeded.set(true);

                    updateBudgetsDB = true;
                } else if(presentInBudgets.contains(budget)) {
                    budget.currentAmount -= lastAmount;
                    updateBudgetsDB = true;
                }

                if(updateBudgetsDB) {
                    SessionCache.budgetsDatabase.updateBudgetRow(
                            budget.sqlRowID,
                            BudgetsDatabase.COLUMN_AMOUNT_CURRENT,
                            budget.currentAmount
                    );
                }
            });

            SessionCache.expensesDatabase.updateExpense(expenseEdit.sqlRowID, expenseEdit);

            ExpensesActivity.expensesPopupSortRef.get().applySort();

            if(tempImageFile != null && tempImageFile.exists()) {
                File file = new File(SessionCache.galleryDirectory, SessionCache.TEMP_IMAGE_NAME);

                if(tempImageFile.exists()) {
                    file.renameTo(new File(SessionCache.galleryDirectory, expenseEdit.sqlRowID + ".jpg"));
                    GalleryActivity.refreshThumbnails = true;
                }
            } else if(deletedImage) {
                File file = new File(SessionCache.galleryDirectory, expenseEdit.sqlRowID + ".jpg");

                if(file.exists()) {
                    file.delete();
                    GalleryActivity.refreshThumbnails = true;
                }
            }

            if(!budgetExceeded.get()) {
                finish();
                overridePendingTransition(0, 0);
                rvExpensesListRef.get().scrollToPosition(0);

                if(exitListener != null)
                    exitListener.run();
            } else {
                UIUtils.Dialog.showPrompt0(
                        view,
                        null,
                        "Budget Exceeded",
                        "You have exceeded one of your budget entries!",
                        "Acknowledge",
                        btnView -> {
                            finish();
                            overridePendingTransition(0, 0);
                            rvExpensesListRef.get().scrollToPosition(0);

                            if(exitListener != null)
                                exitListener.run();
                        }
                );
            }
        });

        tvDelete.setOnClickListener(view -> {
            UIUtils.Dialog.showPrompt1(
                    view,
                    null,
                    "Delete Expense Entry",
                    "Are you sure you want to delete this entry?\nThis action cannot be undone.",
                    "Cancel",
                    "Delete",
                    ColorStateList.valueOf(ContextCompat.getColor(this, R.color.btn_background_neutral)).getDefaultColor(),
                    ColorStateList.valueOf(ContextCompat.getColor(this, R.color.btn_background_danger)).getDefaultColor(),
                    null,
                    btn1View -> {
                        SessionCache.budgetsItems.forEach(budget -> {
                            int budgetDateEnd = budget.endDate.getUniqueValue();
                            int budgetDateStart = budget.startDate.getUniqueValue();
                            int expenseDate = expenseEdit.date.getUniqueValue();

                            if(budget.expensesCategoryID == expenseEdit.expensesCategoryID
                                    && budgetDateStart <= expenseDate
                                    && budgetDateEnd >= expenseDate) {
                                budget.currentAmount -= expenseEdit.amount;

                                SessionCache.budgetsDatabase.updateBudgetRow(
                                        budget.sqlRowID,
                                        BudgetsDatabase.COLUMN_AMOUNT_CURRENT,
                                        budget.currentAmount
                                );
                            }
                        });

                        SessionCache.expensesDatabase.deleteExpense(expenseEdit.sqlRowID);
                        SessionCache.expensesItems.remove(expenseEdit.listIndex);

                        for(int i = expenseEdit.listIndex; i < SessionCache.expensesItems.size(); i++)
                            SessionCache.expensesItems.get(i).listIndex = i;

                        ExpensesActivity.expensesPopupSortRef.get().applySort();

                        File file = new File(SessionCache.galleryDirectory, expenseEdit.sqlRowID + ".jpg");

                        if(file.exists()) {
                            file.delete();
                            GalleryActivity.refreshThumbnails = true;
                        }

                        if(tempImageFile.exists()) {
                            tempImageFile.delete();
                            GalleryActivity.refreshThumbnails = true;
                        }

                        finish();
                        overridePendingTransition(0, 0);

                        if(exitListener != null)
                            exitListener.run();
                    }
            );
        });

        llAddImage.setOnClickListener(view -> {
            UIUtils.Dialog.showPrompt2(
                    view,
                    null,
                    (hasImagePreview ? "Edit" : "Add") + " Expense Image",
                    "Choose where you want to retrieve your image from.",
                    "Use Camera",
                    "Camera Roll",
                    "Cancel",
                    ColorStateList.valueOf(ContextCompat.getColor(this, R.color.btn_background)).getDefaultColor(),
                    ColorStateList.valueOf(ContextCompat.getColor(this, R.color.btn_background)).getDefaultColor(),
                    ColorStateList.valueOf(ContextCompat.getColor(this, R.color.btn_background_neutral)).getDefaultColor(),
                    (btn0View) -> camera.launchCamera(new File(SessionCache.galleryDirectory, SessionCache.TEMP_IMAGE_NAME)),
                    (btn1View) -> galleryPicker.launchGallery(new File(SessionCache.galleryDirectory, SessionCache.TEMP_IMAGE_NAME)),
                    null
            );
        });

        llDeleteImage.setOnClickListener(view -> {
            imgThumbnail.setImageTintList(ColorStateList.valueOf(SessionCache.Color.icoGrayed));
            Glide.with(imgThumbnail.getContext()).clear(imgThumbnail);
            imgThumbnail.setImageResource(R.drawable.ic_image_missing);

            tvAddImage.setText("Add");
            llDeleteImage.setVisibility(LinearLayout.GONE);

            deletedImage = true;
            hasImagePreview = false;
        });

        imgThumbnail.setOnClickListener(view -> {
            File imageFile = tempImageFile != null && tempImageFile.exists()
                    ? tempImageFile
                    : new File(SessionCache.galleryDirectory, expenseEdit.sqlRowID + ".jpg");

            if(!imageFile.exists())
                return;

            Intent intent = new Intent(this, GalleryImageViewerActivity.class);

            GalleryImageViewerActivity.imageFile = imageFile;
            startActivity(intent);
        });
    }

    private void initRecyclerViews() {
        Arrays.stream(ExpensesCategory.getListOrder()).forEach(categoryID ->
            categoryDropdownComposite.items.add(ExpensesCategory.getExpensesCategoryName(categoryID))
        );

        UIUtils.CompositeInstantiator.categoryDropdown(categoryDropdownComposite, tfCategory);
    }

}
