package com.ropkastudios.android.thegeographyapp.database;

public class DbSchema {
    public static final class InformationCardTable {
        public static final String NAME = "data";

        public static final class Cols {
            public static final String UUID = "uuid";
            public static final String CARD_INFORMATION_TITLE = "title";
            public static final String PARAGRAPH = "paragraph";
            public static final String BUTTON_TITLE = "button_title";
            public static final String DELETABLE = "deletable";
        }
    }

    public static final class InformationSubCategoryCardTable {
        public static final String NAME = "data_subcategory";

        public static final class Cols {
            public static final String UUID = "sub_cat_uuid";
            public static final String CATEGORY = "sub_cat_category";
            public static final String CARD_TITLE = "sub_cat_title";
            public static final String DELETABLE = "sub_cat_deletable";
            public static final String CONTENTS = "sub_cat_contents";
            public static final String NOTES = "sub_cat_notes";
        }
    }

    public static final class RandomValuesTable {
        public static final String NAME = "data_random_values";

        public static final class Cols {
            public static final String UUID = "random_data_uuid";

            public static final String INTS = "random_data_int";


            public static final String BOOLEANS = "random_data_bool";
            /**
             * First boolean is the toggle for the EditAction - button holder in SubcategoryContentFragment.
             */

            public static final String STRINGS = "random_data_texts";
        }
    }

    public static final class SavedWebTable {
        public static final String NAME = "data_saved_websites";

        public static final class Cols {
            public static final String UUID = "savweb_uuid";
            public static final String TITLE = "savweb_title";
            public static final String URL = "savweb_url";
            public static final String IMG_URL = "savweb_img_url";
            public static final String DATE = "savweb_date";
            public static final String DATE_VAL = "savweb_date_val";
            public static final String CATEGORY = "savweb_category";
            public static final String DESCRIPTION = "savweb_description";
        }
    }
}
