package com.ropkastudios.android.thegeographyapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;
import java.util.UUID;
public class InformationSubcategoryFragment extends BackPressedFragment {
    private static final String TAG = "InfoSubCategoryFragment";

    private RecyclerView mCardRecyclerView;
    private CardAdapter mAdapter;
    private CardAdapter mDeleteAdapter;
    private CardAdapter mUpdateAdapter;
    private CardAdapter mQueryAdapter;
    private TextView mPopupTextView;

    MenuItem searchItem;

    private boolean isUndoDeleteCardMode = false;
    private boolean isUpdateCardMode = false;
    Callbacks mCallbacks;

    private static final String DIALOG_CREATE_CARD = "SubDialogCreateCard";
    private static final String DIALOG_CONFIRM_DELETE_CARD = "SubDialogConfirmDeleteCard";
    private static final String DIALOG_UPDATE_CARD = "SubDialogUpdateCard";
    private static final int REQUEST_CREATE_CARD = 11;
    private static final int REQUEST_CONFIRM_DELETE_CARD = 12;
    private static final int REQUEST_UPDATE_CARD = 13;

    public interface Callbacks {
        void finishActivity();
    }

    public static InformationSubcategoryFragment newInstance() {
        return new InformationSubcategoryFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof Activity) {
            mCallbacks = (Callbacks) context;
        }
    }

    @Override
    public boolean onBackPressed() {
        if (isUndoDeleteCardMode || isUpdateCardMode) {
            resetToNormalAdapter();
            return true;
        } else if (getActivity() instanceof MainMenuActivity) {
            MainMenuActivity activity = (MainMenuActivity) getActivity();
            InformationFragment fragment = InformationFragment.newInstance();
            FragmentManager fm = getActivity().getSupportFragmentManager();
            SearchView view = (SearchView) searchItem.getActionView();
            view.setIconified(true);
            activity.replaceFragmentInActivity(MainMenuActivity.FRAGMENT_CONTAINER, fragment,
                    fm, "Information");
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        SubcategoryCardInfoLab cardLab = SubcategoryCardInfoLab.get(getActivity());
        List<SubcategoryCardInfoHolder> cardInfoHolderList = cardLab.getCardInfos();
        if (cardInfoHolderList.size() == 0) {
            Resources res = getResources();
            enterListInDatabase(new String[] {
                            res.getString(R.string.afghanistan),
                            res.getString(R.string.albania),
                            res.getString(R.string.algeria),
                            res.getString(R.string.andorra),
                            res.getString(R.string.angola),
                            res.getString(R.string.antiguaandbarbuda),
                            res.getString(R.string.argentina),
                            res.getString(R.string.armenia),
                            res.getString(R.string.australia),
                            res.getString(R.string.austria),
                            res.getString(R.string.azerbaijan),
                            res.getString(R.string.thebahamas),
                            res.getString(R.string.bahrain),
                            res.getString(R.string.bangladesh),
                            res.getString(R.string.barbados),
                            res.getString(R.string.belarus),
                            res.getString(R.string.belgium),
                            res.getString(R.string.belize),
                            res.getString(R.string.benin),
                            res.getString(R.string.bhutan),
                            res.getString(R.string.bolivia),
                            res.getString(R.string.bosniaandherzegovina),
                            res.getString(R.string.botswana),
                            res.getString(R.string.brazil),
                            res.getString(R.string.brunei),
                            res.getString(R.string.bulgaria),
                            res.getString(R.string.burkinafaso),
                            res.getString(R.string.burma),
                            res.getString(R.string.burundi),
                            res.getString(R.string.cambodia),
                            res.getString(R.string.cameroon),
                            res.getString(R.string.canada),
                            res.getString(R.string.capeverde),
                            res.getString(R.string.centralafricanrepublic),
                            res.getString(R.string.chad),
                            res.getString(R.string.chile),
                            res.getString(R.string.china),
                            res.getString(R.string.colombia),
                            res.getString(R.string.comoros),
                            res.getString(R.string.congodemocraticrepublicofthe),
                            res.getString(R.string.congorepublicofthe),
                            res.getString(R.string.costarica),
                            res.getString(R.string.cotedivoire),
                            res.getString(R.string.croatia),
                            res.getString(R.string.cuba),
                            res.getString(R.string.cyprus),
                            res.getString(R.string.czechrepublic),
                            res.getString(R.string.denmark),
                            res.getString(R.string.djibouti),
                            res.getString(R.string.dominica),
                            res.getString(R.string.dominicanrepublic),
                            res.getString(R.string.easttimor),
                            res.getString(R.string.ecuador),
                            res.getString(R.string.egypt),
                            res.getString(R.string.elsalvador),
                            res.getString(R.string.equatorialguinea),
                            res.getString(R.string.eritrea),
                            res.getString(R.string.estonia),
                            res.getString(R.string.ethiopia),
                            res.getString(R.string.fiji),
                            res.getString(R.string.finland),
                            res.getString(R.string.france),
                            res.getString(R.string.gabon),
                            res.getString(R.string.gambiathe),
                            res.getString(R.string.georgia),
                            res.getString(R.string.germany),
                            res.getString(R.string.ghana),
                            res.getString(R.string.greece),
                            res.getString(R.string.grenada),
                            res.getString(R.string.guatemala),
                            res.getString(R.string.guinea),
                            res.getString(R.string.guineabissau),
                            res.getString(R.string.guyana),
                            res.getString(R.string.haiti),
                            res.getString(R.string.holysee),
                            res.getString(R.string.honduras),
                            res.getString(R.string.hungary),
                            res.getString(R.string.iceland),
                            res.getString(R.string.india),
                            res.getString(R.string.indonesia),
                            res.getString(R.string.iran),
                            res.getString(R.string.iraq),
                            res.getString(R.string.ireland),
                            res.getString(R.string.israel),
                            res.getString(R.string.italy),
                            res.getString(R.string.jamaica),
                            res.getString(R.string.japan),
                            res.getString(R.string.jordan),
                            res.getString(R.string.kazakhstan),
                            res.getString(R.string.kenya),
                            res.getString(R.string.kiribati),
                            res.getString(R.string.koreanorth),
                            res.getString(R.string.koreasouth),
                            res.getString(R.string.kosovo),
                            res.getString(R.string.kuwait),
                            res.getString(R.string.kyrgyzstan),
                            res.getString(R.string.laos),
                            res.getString(R.string.latvia),
                            res.getString(R.string.lebanon),
                            res.getString(R.string.lesotho),
                            res.getString(R.string.liberia),
                            res.getString(R.string.libya),
                            res.getString(R.string.liechtenstein),
                            res.getString(R.string.lithuania),
                            res.getString(R.string.luxembourg),
                            res.getString(R.string.macedonia),
                            res.getString(R.string.madagascar),
                            res.getString(R.string.malawi),
                            res.getString(R.string.malaysia),
                            res.getString(R.string.maldives),
                            res.getString(R.string.mali),
                            res.getString(R.string.malta),
                            res.getString(R.string.marshallislands),
                            res.getString(R.string.mauritania),
                            res.getString(R.string.mauritius),
                            res.getString(R.string.mexico),
                            res.getString(R.string.micronesia),
                            res.getString(R.string.moldova),
                            res.getString(R.string.monaco),
                            res.getString(R.string.mongolia),
                            res.getString(R.string.montenegro),
                            res.getString(R.string.morocco),
                            res.getString(R.string.mozambique),
                            res.getString(R.string.namibia),
                            res.getString(R.string.nauru),
                            res.getString(R.string.nepal),
                            res.getString(R.string.netherlands),
                            res.getString(R.string.newzealand),
                            res.getString(R.string.nicaragua),
                            res.getString(R.string.niger),
                            res.getString(R.string.nigeria),
                            res.getString(R.string.norway),
                            res.getString(R.string.oman),
                            res.getString(R.string.pakistan),
                            res.getString(R.string.palau),
                            res.getString(R.string.palestine),
                            res.getString(R.string.panama),
                            res.getString(R.string.papuanewguinea),
                            res.getString(R.string.paraguay),
                            res.getString(R.string.peru),
                            res.getString(R.string.philippines),
                            res.getString(R.string.poland),
                            res.getString(R.string.portugal),
                            res.getString(R.string.qatar),
                            res.getString(R.string.romania),
                            res.getString(R.string.russia),
                            res.getString(R.string.rwanda),
                            res.getString(R.string.saintkittsandnevis),
                            res.getString(R.string.saintlucia),
                            res.getString(R.string.saintvincentandthegrenadines),
                            res.getString(R.string.samoa),
                            res.getString(R.string.sanmarino),
                            res.getString(R.string.saotomeandprincipe),
                            res.getString(R.string.saudiarabia),
                            res.getString(R.string.senegal),
                            res.getString(R.string.serbia),
                            res.getString(R.string.seychelles),
                            res.getString(R.string.sierraleone),
                            res.getString(R.string.singapore),
                            res.getString(R.string.slovakia),
                            res.getString(R.string.slovenia),
                            res.getString(R.string.solomonislands),
                            res.getString(R.string.somalia),
                            res.getString(R.string.southafrica),
                            res.getString(R.string.southsudan),
                            res.getString(R.string.spain),
                            res.getString(R.string.srilanka),
                            res.getString(R.string.sudan),
                            res.getString(R.string.suriname),
                            res.getString(R.string.swaziland),
                            res.getString(R.string.sweden),
                            res.getString(R.string.switzerland),
                            res.getString(R.string.syria),
                            res.getString(R.string.taiwan),
                            res.getString(R.string.tajikistan),
                            res.getString(R.string.tanzania),
                            res.getString(R.string.thailand),
                            res.getString(R.string.togo),
                            res.getString(R.string.tonga),
                            res.getString(R.string.trinidadandtobago),
                            res.getString(R.string.tunisia),
                            res.getString(R.string.turkey),
                            res.getString(R.string.turkmenistan),
                            res.getString(R.string.tuvalu),
                            res.getString(R.string.uganda),
                            res.getString(R.string.ukraine),
                            res.getString(R.string.unitedarabemirates),
                            res.getString(R.string.unitedkingdom),
                            res.getString(R.string.unitedstates),
                            res.getString(R.string.uruguay),
                            res.getString(R.string.uzbekistan),
                            res.getString(R.string.vanuatu),
                            res.getString(R.string.venezuela),
                            res.getString(R.string.vietnam),
                            res.getString(R.string.yemen),
                            res.getString(R.string.zambia),
                            res.getString(R.string.zimbabwe)
                    }, false, getString(R.string.countries),
                    new int[] {
                            R.string.afghanistan_contents,
                            R.string.albania_contents,
                            R.string.algeria_contents,
                            R.string.andorra_contents,
                            R.string.angola_contents,
                            R.string.antigua_and_barbuda_contents,
                            R.string.argentina_contents,
                            R.string.armenia_contents,
                            R.string.australia_contents,
                            R.string.austria_contents,
                            R.string.azerbaijan_contents,
                            R.string.the_bahamas_contents,
                            R.string.bahrain_contents,
                            R.string.bangladesh_contents,
                            R.string.barbados_contents,
                            R.string.belarus_contents,
                            R.string.belgium_contents,
                            R.string.belize_contents,
                            R.string.benin_contents,
                            R.string.bhutan_contents,
                            R.string.bolivia_contents,
                            R.string.bosnia_and_herzegovina_contents,
                            R.string.botswana_contents,
                            R.string.brazil_contents,
                            R.string.brunei_contents,
                            R.string.bulgaria_contents,
                            R.string.burkina_faso_contents,
                            R.string.burma_contents,
                            R.string.burundi_contents,
                            R.string.cambodia_contents,
                            R.string.cameroon_contents,
                            R.string.canada_contents,
                            R.string.cape_verde_contents,
                            R.string.central_african_republic_contents,
                            R.string.chad_contents,
                            R.string.chile_contents,
                            R.string.china_contents,
                            R.string.colombia_contents,
                            R.string.comoros_contents,
                            R.string.democratic_republic_of_the_congo_contents,
                            R.string.republic_of_the_congo_contents,
                            R.string.costa_rica_contents,
                            R.string.ivory_coast_contents,
                            R.string.croatia_contents,
                            R.string.cuba_contents,
                            R.string.cyprus_contents,
                            R.string.czech_republic_contents,
                            R.string.denmark_contents,
                            R.string.djibouti_contents,
                            R.string.dominica_contents,
                            R.string.dominican_republic_contents,
                            R.string.east_timor_contents,
                            R.string.ecuador_contents,
                            R.string.egypt_contents,
                            R.string.el_salvador_contents,
                            R.string.equatorial_guinea_contents,
                            R.string.eritrea_contents,
                            R.string.estonia_contents,
                            R.string.ethiopia_contents,
                            R.string.fiji_contents,
                            R.string.finland_contents,
                            R.string.france_contents,
                            R.string.gabon_contents,
                            R.string.the_gambia_contents,
                            R.string.georgia_country_contents,
                            R.string.germany_contents,
                            R.string.ghana_contents,
                            R.string.greece_contents,
                            R.string.grenada_contents,
                            R.string.guatemala_contents,
                            R.string.guinea_contents,
                            R.string.guinea_bissau_contents,
                            R.string.guyana_contents,
                            R.string.haiti_contents,
                            R.string.vatican_city_contents,
                            R.string.honduras_contents,
                            R.string.hungary_contents,
                            R.string.iceland_contents,
                            R.string.india_contents,
                            R.string.indonesia_contents,
                            R.string.iran_contents,
                            R.string.iraq_contents,
                            R.string.ireland_contents,
                            R.string.israel_contents,
                            R.string.italy_contents,
                            R.string.jamaica_contents,
                            R.string.japan_contents,
                            R.string.jordan_contents,
                            R.string.kazakhstan_contents,
                            R.string.kenya_contents,
                            R.string.kiribati_contents,
                            R.string.north_korea_contents,
                            R.string.south_korea_contents,
                            R.string.kosovo_contents,
                            R.string.kuwait_contents,
                            R.string.kyrgyzstan_contents,
                            R.string.laos_contents,
                            R.string.latvia_contents,
                            R.string.lebanon_contents,
                            R.string.lesotho_contents,
                            R.string.liberia_contents,
                            R.string.libya_contents,
                            R.string.liechtenstein_contents,
                            R.string.lithuania_contents,
                            R.string.luxembourg_contents,
                            R.string.republic_of_macedonia_contents,
                            R.string.madagascar_contents,
                            R.string.malawi_contents,
                            R.string.malaysia_contents,
                            R.string.maldives_contents,
                            R.string.mali_contents,
                            R.string.malta_contents,
                            R.string.marshall_islands_contents,
                            R.string.mauritania_contents,
                            R.string.mauritius_contents,
                            R.string.mexico_contents,
                            R.string.federated_states_of_micronesia_contents,
                            R.string.moldova_contents,
                            R.string.monaco_contents,
                            R.string.mongolia_contents,
                            R.string.montenegro_contents,
                            R.string.morocco_contents,
                            R.string.mozambique_contents,
                            R.string.namibia_contents,
                            R.string.nauru_contents,
                            R.string.nepal_contents,
                            R.string.netherlands_contents,
                            R.string.new_zealand_contents,
                            R.string.nicaragua_contents,
                            R.string.niger_contents,
                            R.string.nigeria_contents,
                            R.string.norway_contents,
                            R.string.oman_contents,
                            R.string.pakistan_contents,
                            R.string.palau_contents,
                            R.string.the_state_of_palestine_contents,
                            R.string.panama_contents,
                            R.string.papua_new_guinea_contents,
                            R.string.paraguay_contents,
                            R.string.peru_contents,
                            R.string.philippines_contents,
                            R.string.poland_contents,
                            R.string.portugal_contents,
                            R.string.qatar_contents,
                            R.string.romania_contents,
                            R.string.russia_contents,
                            R.string.rwanda_contents,
                            R.string.saint_kitts_and_nevis_contents,
                            R.string.saint_lucia_contents,
                            R.string.saint_vincent_and_the_grenadines_contents,
                            R.string.samoa,
                            R.string.san_marino_contents,
                            R.string.sao_tome_and_principe_contents,
                            R.string.saudi_arabia_contents,
                            R.string.senegal_contents,
                            R.string.serbia_contents,
                            R.string.seychelles_contents,
                            R.string.sierra_leone_contents,
                            R.string.singapore_contents,
                            R.string.slovakia_contents,
                            R.string.slovenia_contents,
                            R.string.solomon_islands_contents,
                            R.string.somalia_contents,
                            R.string.south_africa_contents,
                            R.string.south_sudan_contents,
                            R.string.spain_contents,
                            R.string.sri_lanka_contents,
                            R.string.sudan_contents,
                            R.string.suriname_contents,
                            R.string.swaziland_contents,
                            R.string.sweden_contents,
                            R.string.switzerland_contents,
                            R.string.syria_contents,
                            R.string.taiwan_contents,
                            R.string.tajikistan_contents,
                            R.string.tanzania_contents,
                            R.string.thailand_contents,
                            R.string.togo_contents,
                            R.string.tonga_contents,
                            R.string.trinidad_and_tobago_contents,
                            R.string.tunisia_contents,
                            R.string.turkey_contents,
                            R.string.turkmenistan_contents,
                            R.string.tuvalu_contents,
                            R.string.uganda_contents,
                            R.string.ukraine_contents,
                            R.string.united_arab_emirates_contents,
                            R.string.united_kingdom_contents,
                            R.string.united_states_contents,
                            R.string.uruguay_contents,
                            R.string.uzbekistan_contents,
                            R.string.vanuatu_contents,
                            R.string.venezuela_contents,
                            R.string.vietnam_contents,
                            R.string.yemen_contents,
                            R.string.zambia_contents,
                            R.string.zimbabwe_contents
                    },
                    cardLab);
            enterListInDatabase(new String[]{
                            res.getString(R.string.pacific_ocean),
                            res.getString(R.string.atlantic_ocean),
                            res.getString(R.string.indian_ocean),
                            res.getString(R.string.arctic_ocean),
                            res.getString(R.string.southern_ocean)
                    }, false, res.getString(R.string.oceans),
                    new String[]{
                            res.getString(R.string.pacific_ocean_contents),
                            res.getString(R.string.atlantic_ocean_contents),
                            res.getString(R.string.indian_ocean_contents),
                            res.getString(R.string.arctic_ocean_contents),
                            res.getString(R.string.southern_ocean_contents)
                    }, cardLab);
            enterListInDatabase(new String[]{
                            res.getString(R.string.north_america),
                            res.getString(R.string.south_america),
                            res.getString(R.string.africa),
                            res.getString(R.string.asia),
                            res.getString(R.string.europe),
                            res.getString(R.string.australia),
                            res.getString(R.string.antarctica),
                            res.getString(R.string.oceania),
                    }, false, res.getString(R.string.continents),
                    new String[] {
                            res.getString(R.string.north_america_contents),
                            res.getString(R.string.south_america_contents),
                            res.getString(R.string.africa_contents),
                            res.getString(R.string.asia_contents),
                            res.getString(R.string.europe_contents),
                            res.getString(R.string.australia_contents),
                            res.getString(R.string.antarctica_contents),
                            res.getString(R.string.oceania_contents)
                    }, cardLab);
            enterListInDatabase(new String[]{
                        res.getString(R.string.amazon_river),
                        res.getString(R.string.ucayali_river),
                        res.getString(R.string.apurímac_river),
                        res.getString(R.string.nile_river),
                        res.getString(R.string.kagera_river),
                        res.getString(R.string.yangtze_river),
                        res.getString(R.string.mississippi_river),
                        res.getString(R.string.missouri_river),
                        res.getString(R.string.jefferson_river),
                        res.getString(R.string.yenisei_river),
                        res.getString(R.string.angara),
                        res.getString(R.string.angara_river),
                        res.getString(R.string.selenge_river),
                        res.getString(R.string.yellow_river),
                        res.getString(R.string.ob_river),
                        res.getString(R.string.irtysh_river),
                        res.getString(R.string.paraná_river),
                        res.getString(R.string.río_de_la_plata),
                        res.getString(R.string.congo_river),
                        res.getString(R.string.chambeshi_river),
                        res.getString(R.string.amur_river),
                        res.getString(R.string.lena_river),
                        res.getString(R.string.mekong_river),
                        res.getString(R.string.mackenzie_river),
                        res.getString(R.string.slave_river),
                        res.getString(R.string.peace_river),
                        res.getString(R.string.finlay_river),
                        res.getString(R.string.niger_river),
                        res.getString(R.string.murray_river),
                        res.getString(R.string.darling_river),
                        res.getString(R.string.tocantins_river),
                        res.getString(R.string.araguaia_river),
                        res.getString(R.string.volga_river),
                        res.getString(R.string.madeira_river),
                        res.getString(R.string.rocha_river),
                        res.getString(R.string.caine_river),
                        res.getString(R.string.mamoré_river),
                        res.getString(R.string.purús_river),
                        res.getString(R.string.yukon_river),
                        res.getString(R.string.são_francisco_river),
                        res.getString(R.string.syr_darya_river),
                        res.getString(R.string.naryn_river),
                        res.getString(R.string.salween_river),
                        res.getString(R.string.saint_lawrence_river),
                        res.getString(R.string.great_lakes_river),
                        res.getString(R.string.rio_grande),
                        res.getString(R.string.lower_tunguska_river),
                        res.getString(R.string.brahmaputra_river),
                        res.getString(R.string.tsangpo_river),
                        res.getString(R.string.danube_river),
                        res.getString(R.string.breg_river),
                        res.getString(R.string.zambezi_river),
                        res.getString(R.string.vilyuy_river),
                        res.getString(R.string.amu_darya),
                        res.getString(R.string.ganges_river),
                        res.getString(R.string.hooghly_river),
                        res.getString(R.string.padma_river),
                        res.getString(R.string.nelson_river),
                        res.getString(R.string.saskatchewan_river),
                        res.getString(R.string.kolyma_river),
                        res.getString(R.string.pilcomayo_river),
                        res.getString(R.string.upper_ob),
                        res.getString(R.string.katun_river),
                        res.getString(R.string.ishim_river),
                        res.getString(R.string.ural_river),
                        res.getString(R.string.arkansas_river),
                        res.getString(R.string.colorado_river),
                        res.getString(R.string.olenyok_river),
                        res.getString(R.string.dnieper),
                        res.getString(R.string.dnieper_river),
                        res.getString(R.string.dniester_river),
                        res.getString(R.string.paraguay_river),
                        res.getString(R.string.japurá_river),
                        res.getString(R.string.juruá_river),
                        res.getString(R.string.ayeyarwady_river),
                        res.getString(R.string.kasai_river),
                        res.getString(R.string.ohio_river),
                        res.getString(R.string.allegheny_river),
                        res.getString(R.string.xingu_river),
                        res.getString(R.string.orange_river),
                        res.getString(R.string.aldan_river),
                        res.getString(R.string.red_river_of_the_south),
                        res.getString(R.string.orinoco_river),
                        res.getString(R.string.tarim_river),
                        res.getString(R.string.khatanga_river),
                        res.getString(R.string.blue_nile),
                        res.getString(R.string.murrumbidgee_river),
                        res.getString(R.string.snake_river),
                        res.getString(R.string.limpopo_river),
                        res.getString(R.string.kama_river),
                        res.getString(R.string.songhua_river),
                        res.getString(R.string.tigris_river),
                        res.getString(R.string.uruguay_river),
                        res.getString(R.string.senegal_river),
                        res.getString(R.string.indigirka_river),
                        res.getString(R.string.guaporé_river),
                        res.getString(R.string.tapajós_river),
                        res.getString(R.string.godavari_river),
                        res.getString(R.string.benue_river),
                        res.getString(R.string.magdalena_river),
                        res.getString(R.string.putumayo_river),
                        res.getString(R.string.shebelle_river),
                        res.getString(R.string.jubba_river),
                        res.getString(R.string.platte_river),
                        res.getString(R.string.tobol_river),
                        res.getString(R.string.oder_river),
                        res.getString(R.string.vistula_river),
                        res.getString(R.string.tagus_river),
                        res.getString(R.string.gila_river),
                        res.getString(R.string.yellowstone_river),
                        res.getString(R.string.vaal_river),
                        res.getString(R.string.ottawa_river),
                        res.getString(R.string.krishna_river),
                        res.getString(R.string.fraser_river),
                        res.getString(R.string.marañón_river),
                        res.getString(R.string.barcoo_river),
                        res.getString(R.string.cooper_creek),
                        res.getString(R.string.yamuna_river)
                    }, false, res.getString(R.string.major_world_rivers),
                    new String[]{
                        res.getString(R.string.amazon_river_contents),
                        res.getString(R.string.ucayali_river_contents),
                        res.getString(R.string.apurímac_river_contents),
                        res.getString(R.string.nile_river_contents),
                        res.getString(R.string.kagera_river_contents),
                        res.getString(R.string.yangtze_river_contents),
                        res.getString(R.string.mississippi_river_contents),
                        res.getString(R.string.missouri_river_contents),
                        res.getString(R.string.jefferson_river_contents),
                        res.getString(R.string.yenisei_river_contents),
                        res.getString(R.string.angara_contents),
                        res.getString(R.string.angara_river_contents),
                        res.getString(R.string.selenge_river_contents),
                        res.getString(R.string.yellow_river_contents),
                        res.getString(R.string.ob_river_contents),
                        res.getString(R.string.irtysh_river_contents),
                        res.getString(R.string.paraná_river_contents),
                        res.getString(R.string.río_de_la_plata_contents),
                        res.getString(R.string.congo_river_contents),
                        res.getString(R.string.chambeshi_river_contents),
                        res.getString(R.string.amur_river_contents),
                        res.getString(R.string.lena_river_contents),
                        res.getString(R.string.mekong_river_contents),
                        res.getString(R.string.mackenzie_river_contents),
                        res.getString(R.string.slave_river_contents),
                        res.getString(R.string.peace_river_contents),
                        res.getString(R.string.finlay_river_contents),
                        res.getString(R.string.niger_river_contents),
                        res.getString(R.string.murray_river_contents),
                        res.getString(R.string.darling_river_contents),
                        res.getString(R.string.tocantins_river_contents),
                        res.getString(R.string.araguaia_river_contents),
                        res.getString(R.string.volga_river_contents),
                        res.getString(R.string.madeira_river_contents),
                        res.getString(R.string.rocha_river_contents),
                        res.getString(R.string.caine_river_contents),
                        res.getString(R.string.mamoré_river_contents),
                        res.getString(R.string.purús_river_contents),
                        res.getString(R.string.yukon_river_contents),
                        res.getString(R.string.são_francisco_river_contents),
                        res.getString(R.string.syr_darya_river_contents),
                        res.getString(R.string.naryn_river_contents),
                        res.getString(R.string.salween_river_contents),
                        res.getString(R.string.saint_lawrence_river_contents),
                        res.getString(R.string.great_lakes_river_contents),
                        res.getString(R.string.rio_grande_contents),
                        res.getString(R.string.lower_tunguska_river_contents),
                        res.getString(R.string.brahmaputra_river_contents),
                        res.getString(R.string.tsangpo_river_contents),
                        res.getString(R.string.danube_river_contents),
                        res.getString(R.string.breg_river_contents),
                        res.getString(R.string.zambezi_river_contents),
                        res.getString(R.string.vilyuy_river_contents),
                        res.getString(R.string.amu_darya_contents),
                        res.getString(R.string.ganges_river_contents),
                        res.getString(R.string.hooghly_river_contents),
                        res.getString(R.string.padma_river_contents),
                        res.getString(R.string.nelson_river_contents),
                        res.getString(R.string.saskatchewan_river_contents),
                        res.getString(R.string.kolyma_river_contents),
                        res.getString(R.string.pilcomayo_river_contents),
                        res.getString(R.string.upper_ob_contents),
                        res.getString(R.string.katun_river_contents),
                        res.getString(R.string.ishim_river_contents),
                        res.getString(R.string.ural_river_contents),
                        res.getString(R.string.arkansas_river_contents),
                        res.getString(R.string.colorado_river_contents),
                        res.getString(R.string.olenyok_river_contents),
                        res.getString(R.string.dnieper_contents),
                        res.getString(R.string.dnieper_river_contents),
                        res.getString(R.string.dniester_river_contents),
                        res.getString(R.string.paraguay_river_contents),
                        res.getString(R.string.japurá_river_contents),
                        res.getString(R.string.juruá_river_contents),
                        res.getString(R.string.ayeyarwady_river_contents),
                        res.getString(R.string.kasai_river_contents),
                        res.getString(R.string.ohio_river_contents),
                        res.getString(R.string.allegheny_river_contents),
                        res.getString(R.string.xingu_river_contents),
                        res.getString(R.string.orange_river_contents),
                        res.getString(R.string.aldan_river_contents),
                        res.getString(R.string.red_river_of_the_south_contents),
                        res.getString(R.string.orinoco_river_contents),
                        res.getString(R.string.tarim_river_contents),
                        res.getString(R.string.khatanga_river_contents),
                        res.getString(R.string.blue_nile_contents),
                        res.getString(R.string.murrumbidgee_river_contents),
                        res.getString(R.string.snake_river_contents),
                        res.getString(R.string.limpopo_river_contents),
                        res.getString(R.string.kama_river_contents),
                        res.getString(R.string.songhua_river_contents),
                        res.getString(R.string.tigris_river_contents),
                        res.getString(R.string.uruguay_river_contents),
                        res.getString(R.string.senegal_river_contents),
                        res.getString(R.string.indigirka_river_contents),
                        res.getString(R.string.guaporé_river_contents),
                        res.getString(R.string.tapajós_river_contents),
                        res.getString(R.string.godavari_river_contents),
                        res.getString(R.string.benue_river_contents),
                        res.getString(R.string.magdalena_river_contents),
                        res.getString(R.string.putumayo_river_contents),
                        res.getString(R.string.shebelle_river_contents),
                        res.getString(R.string.jubba_river_contents),
                        res.getString(R.string.platte_river_contents),
                        res.getString(R.string.tobol_river_contents),
                        res.getString(R.string.oder_river_contents),
                        res.getString(R.string.vistula_river_contents),
                        res.getString(R.string.tagus_river_contents),
                        res.getString(R.string.gila_river_contents),
                        res.getString(R.string.yellowstone_river_contents),
                        res.getString(R.string.vaal_river_contents),
                        res.getString(R.string.ottawa_river_contents),
                        res.getString(R.string.krishna_river_contents),
                        res.getString(R.string.fraser_river_contents),
                        res.getString(R.string.marañón_river_contents),
                        res.getString(R.string.barcoo_river_contents),
                        res.getString(R.string.cooper_creek_contents),
                        res.getString(R.string.yamuna_river)
                    }, cardLab);
            enterListInDatabase(new String[]{
                        res.getString(R.string.tokyo),
                        res.getString(R.string.jakarta),
                        res.getString(R.string.delhi),
                        res.getString(R.string.manila),
                        res.getString(R.string.seoul),
                        res.getString(R.string.shanghai),
                        res.getString(R.string.karachi),
                        res.getString(R.string.beijing),
                        res.getString(R.string.new_york_city),
                        res.getString(R.string.guangzhou),
                        res.getString(R.string.são_paulo),
                        res.getString(R.string.lima),
                        res.getString(R.string.chengdu),
                        res.getString(R.string.london),
                        res.getString(R.string.nagoya),
                        res.getString(R.string.lahore),
                        res.getString(R.string.chennai),
                        res.getString(R.string.chicago),
                        res.getString(R.string.bogotá),
                        res.getString(R.string.ho_chi_minh_city),
                        res.getString(R.string.hyderabad),
                        res.getString(R.string.bangalore),
                        res.getString(R.string.dongguan),
                        res.getString(R.string.johannesburg),
                        res.getString(R.string.wuhan),
                        res.getString(R.string.taipei),
                        res.getString(R.string.hangzhou),
                        res.getString(R.string.hong_kong),
                        res.getString(R.string.chongqing),
                        res.getString(R.string.ahmedabad),
                        res.getString(R.string.kuala_lumpur),
                        res.getString(R.string.quanzhou),
                        res.getString(R.string.essen),
                        res.getString(R.string.düsseldorf),
                        res.getString(R.string.baghdad),
                        res.getString(R.string.toronto),
                        res.getString(R.string.santiago),
                        res.getString(R.string.dallas),
                        res.getString(R.string.madrid),
                        res.getString(R.string.san_jose),
                        res.getString(R.string.nanjing),
                        res.getString(R.string.shenyang),
                        res.getString(R.string.xian),
                        res.getString(R.string.xianyang),
                        res.getString(R.string.san_francisco),
                        res.getString(R.string.luanda),
                        res.getString(R.string.qingdao),
                        res.getString(R.string.jimo),
                        res.getString(R.string.houston),
                        res.getString(R.string.miami),
                        res.getString(R.string.bandung),
                        res.getString(R.string.riyadh),
                        res.getString(R.string.pune),
                        res.getString(R.string.philadelphia),
                        res.getString(R.string.surat),
                        res.getString(R.string.milan),
                        res.getString(R.string.suzhou),
                        res.getString(R.string.saint_petersburg),
                        res.getString(R.string.khartoum),
                        res.getString(R.string.atlanta),
                        res.getString(R.string.zhengzhou),
                        res.getString(R.string.xingyang),
                        res.getString(R.string.surabaya),
                        res.getString(R.string.harbin),
                        res.getString(R.string.abidjan),
                        res.getString(R.string.yangon),
                        res.getString(R.string.nairobi),
                        res.getString(R.string.barcelona),
                        res.getString(R.string.alexandria),
                        res.getString(R.string.kabul),
                        res.getString(R.string.guadalajara),
                        res.getString(R.string.ankara),
                        res.getString(R.string.belo_horizonte),
                        res.getString(R.string.boston),
                        res.getString(R.string.xiamen),
                        res.getString(R.string.kuwait_city),
                        res.getString(R.string.dar_es_salaam),
                        res.getString(R.string.dalian),
                        res.getString(R.string.accra),
                        res.getString(R.string.monterrey),
                        res.getString(R.string.berlin),
                        res.getString(R.string.sydney),
                        res.getString(R.string.fuzhou),
                        res.getString(R.string.medan),
                        res.getString(R.string.dubai),
                        res.getString(R.string.melbourne),
                        res.getString(R.string.rome),
                        res.getString(R.string.busan),
                        res.getString(R.string.cape_town),
                        res.getString(R.string.jinan),
                        res.getString(R.string.ningbo),
                        res.getString(R.string.hanoi),
                        res.getString(R.string.naples),
                        res.getString(R.string.taiyuan),
                        res.getString(R.string.jeddah),
                        res.getString(R.string.detroit),
                        res.getString(R.string.hefei),
                        res.getString(R.string.changsha),
                        res.getString(R.string.kunming),
                        res.getString(R.string.wuxi),
                        res.getString(R.string.medellin),
                        res.getString(R.string.faisalabad),
                        res.getString(R.string.aleppo),
                        res.getString(R.string.kano),
                        res.getString(R.string.montreal),
                        res.getString(R.string.dakar),
                        res.getString(R.string.athens),
                        res.getString(R.string.changzhou),
                        res.getString(R.string.durban),
                        res.getString(R.string.porto_alegre),
                        res.getString(R.string.jaipur),
                        res.getString(R.string.fortaleza),
                        res.getString(R.string.addis_ababa),
                        res.getString(R.string.changchun),
                        res.getString(R.string.shijiazhuang),
                        res.getString(R.string.recife),
                        res.getString(R.string.mashhad),
                        res.getString(R.string.seattle),
                        res.getString(R.string.casablanca),
                        res.getString(R.string.ürümqi),
                        res.getString(R.string.lucknow),
                        res.getString(R.string.chittagong),
                        res.getString(R.string.wenzhou),
                        res.getString(R.string.ibadan),
                        res.getString(R.string.izmir),
                        res.getString(R.string.curitiba),
                        res.getString(R.string.san_diego),
                        res.getString(R.string.yaoundé),
                        res.getString(R.string.kanpur),
                        res.getString(R.string.zhongshan),
                        res.getString(R.string.tel_aviv),
                        res.getString(R.string.zhangjiagang),
                        res.getString(R.string.jiangyin),
                        res.getString(R.string.jingjiang),
                        res.getString(R.string.washington_dc)
                    }, false, res.getString(R.string.major_world_cities).toLowerCase(),
                    new String[] {
                        res.getString(R.string.tokyo_contents),
                        res.getString(R.string.jakarta_contents),
                        res.getString(R.string.delhi_contents),
                        res.getString(R.string.manila_contents),
                        res.getString(R.string.seoul_contents),
                        res.getString(R.string.shanghai_contents),
                        res.getString(R.string.karachi_contents),
                        res.getString(R.string.beijing_contents),
                        res.getString(R.string.new_york_city_contents),
                        res.getString(R.string.guangzhou_contents),
                        res.getString(R.string.são_paulo_contents),
                        res.getString(R.string.lima_contents),
                        res.getString(R.string.chengdu_contents),
                        res.getString(R.string.london_contents),
                        res.getString(R.string.nagoya_contents),
                        res.getString(R.string.lahore_contents),
                        res.getString(R.string.chennai_contents),
                        res.getString(R.string.chicago_contents),
                        res.getString(R.string.bogotá_contents),
                        res.getString(R.string.ho_chi_minh_city_contents),
                        res.getString(R.string.hyderabad_contents),
                        res.getString(R.string.bangalore_contents),
                        res.getString(R.string.dongguan_contents),
                        res.getString(R.string.johannesburg_contents),
                        res.getString(R.string.wuhan_contents),
                        res.getString(R.string.taipei_contents),
                        res.getString(R.string.hangzhou_contents),
                        res.getString(R.string.hong_kong_contents),
                        res.getString(R.string.chongqing_contents),
                        res.getString(R.string.ahmedabad_contents),
                        res.getString(R.string.kuala_lumpur_contents),
                        res.getString(R.string.quanzhou_contents),
                        res.getString(R.string.essen_contents),
                        res.getString(R.string.düsseldorf_contents),
                        res.getString(R.string.baghdad_contents),
                        res.getString(R.string.toronto_contents),
                        res.getString(R.string.santiago_contents),
                        res.getString(R.string.dallas_contents),
                        res.getString(R.string.madrid_contents),
                        res.getString(R.string.san_jose_contents),
                        res.getString(R.string.nanjing_contents),
                        res.getString(R.string.shenyang_contents),
                        res.getString(R.string.xian_contents),
                        res.getString(R.string.xianyang_contents),
                        res.getString(R.string.san_francisco_contents),
                        res.getString(R.string.luanda_contents),
                        res.getString(R.string.qingdao_contents),
                        res.getString(R.string.jimo_contents),
                        res.getString(R.string.houston_contents),
                        res.getString(R.string.miami_contents),
                        res.getString(R.string.bandung_contents),
                        res.getString(R.string.riyadh_contents),
                        res.getString(R.string.pune_contents),
                        res.getString(R.string.philadelphia_contents),
                        res.getString(R.string.surat_contents),
                        res.getString(R.string.milan_contents),
                        res.getString(R.string.suzhou_contents),
                        res.getString(R.string.saint_petersburg_contents),
                        res.getString(R.string.khartoum_contents),
                        res.getString(R.string.atlanta_contents),
                        res.getString(R.string.zhengzhou_contents),
                        res.getString(R.string.xingyang_contents),
                        res.getString(R.string.surabaya_contents),
                        res.getString(R.string.harbin_contents),
                        res.getString(R.string.abidjan_contents),
                        res.getString(R.string.yangon_contents),
                        res.getString(R.string.nairobi_contents),
                        res.getString(R.string.barcelona_contents),
                        res.getString(R.string.alexandria_contents),
                        res.getString(R.string.kabul_contents),
                        res.getString(R.string.guadalajara_contents),
                        res.getString(R.string.ankara_contents),
                        res.getString(R.string.belo_horizonte_contents),
                        res.getString(R.string.boston_contents),
                        res.getString(R.string.xiamen_contents),
                        res.getString(R.string.kuwait_city_contents),
                        res.getString(R.string.dar_es_salaam_contents),
                        res.getString(R.string.dalian_contents),
                        res.getString(R.string.accra_contents),
                        res.getString(R.string.monterrey_contents),
                        res.getString(R.string.berlin_contents),
                        res.getString(R.string.sydney_contents),
                        res.getString(R.string.fuzhou_contents),
                        res.getString(R.string.medan_contents),
                        res.getString(R.string.dubai_contents),
                        res.getString(R.string.melbourne_contents),
                        res.getString(R.string.rome_contents),
                        res.getString(R.string.busan_contents),
                        res.getString(R.string.cape_town_contents),
                        res.getString(R.string.jinan_contents),
                        res.getString(R.string.ningbo_contents),
                        res.getString(R.string.hanoi_contents),
                        res.getString(R.string.naples_contents),
                        res.getString(R.string.taiyuan_contents),
                        res.getString(R.string.jeddah_contents),
                        res.getString(R.string.detroit_contents),
                        res.getString(R.string.hefei_contents),
                        res.getString(R.string.changsha_contents),
                        res.getString(R.string.kunming_contents),
                        res.getString(R.string.wuxi_contents),
                        res.getString(R.string.medellin_contents),
                        res.getString(R.string.faisalabad_contents),
                        res.getString(R.string.aleppo_contents),
                        res.getString(R.string.kano_contents),
                        res.getString(R.string.montreal_contents),
                        res.getString(R.string.dakar_contents),
                        res.getString(R.string.athens_contents),
                        res.getString(R.string.changzhou_contents),
                        res.getString(R.string.durban_contents),
                        res.getString(R.string.porto_alegre_contents),
                        res.getString(R.string.jaipur_contents),
                        res.getString(R.string.fortaleza_contents),
                        res.getString(R.string.addis_ababa_contents),
                        res.getString(R.string.changchun_contents),
                        res.getString(R.string.shijiazhuang_contents),
                        res.getString(R.string.recife_contents),
                        res.getString(R.string.mashhad_contents),
                        res.getString(R.string.seattle_contents),
                        res.getString(R.string.casablanca_contents),
                        res.getString(R.string.ürümqi_contents),
                        res.getString(R.string.lucknow_contents),
                        res.getString(R.string.chittagong_contents),
                        res.getString(R.string.wenzhou_contents),
                        res.getString(R.string.ibadan_contents),
                        res.getString(R.string.izmir_contents),
                        res.getString(R.string.curitiba_contents),
                        res.getString(R.string.san_diego_contents),
                        res.getString(R.string.yaoundé_contents),
                        res.getString(R.string.kanpur_contents),
                        res.getString(R.string.zhongshan_contents),
                        res.getString(R.string.tel_aviv_contents),
                        res.getString(R.string.zhangjiagang_contents),
                        res.getString(R.string.jiangyin_contents),
                        res.getString(R.string.jingjiang_contents),
                        res.getString(R.string.washington_dc)
                    },
                    cardLab);
            enterListInDatabase(new String[]{
                        res.getString(R.string.greenland),
                        res.getString(R.string.new_guinea),
                        res.getString(R.string.borneo),
                        res.getString(R.string.madagascar_island),
                        res.getString(R.string.baffin_island),
                        res.getString(R.string.sumatra),
                        res.getString(R.string.honshu),
                        res.getString(R.string.great_britain),
                        res.getString(R.string.ellesmere_island),
                        res.getString(R.string.sulawesi),
                        res.getString(R.string.south_island),
                        res.getString(R.string.java),
                        res.getString(R.string.north_island),
                        res.getString(R.string.luzon),
                        res.getString(R.string.newfoundland_island),
                        res.getString(R.string.victoria_island_canada),
                        res.getString(R.string.cuba_island),
                        res.getString(R.string.iceland_island),
                        res.getString(R.string.mindanao),
                        res.getString(R.string.ireland_island),
                        res.getString(R.string.hokkaido),
                        res.getString(R.string.hispaniola),
                        res.getString(R.string.sakhalin),
                        res.getString(R.string.banks_island),
                        res.getString(R.string.tasmania),
                        res.getString(R.string.devon_island),
                        res.getString(R.string.alexander_island),
                        res.getString(R.string.isla_grande_de_tierra_del_fuego),
                        res.getString(R.string.severny_island),
                        res.getString(R.string.berkner_island),
                        res.getString(R.string.axel_heiberg_island),
                        res.getString(R.string.southampton_island),
                        res.getString(R.string.marajó),
                        res.getString(R.string.kyushu),
                        res.getString(R.string.taiwan_island),
                        res.getString(R.string.new_britain),
                        res.getString(R.string.yuzhny_island),
                        res.getString(R.string.hainan),
                        res.getString(R.string.vancouver_island),
                        res.getString(R.string.timor),
                        res.getString(R.string.sicily)
                    }, false, res.getString(R.string.major_world_islands).toLowerCase(),
                    new String[] {
                        res.getString(R.string.greenland_contents),
                        res.getString(R.string.new_guinea_contents),
                        res.getString(R.string.borneo_contents),
                        res.getString(R.string.madagascar_island_contents),
                        res.getString(R.string.baffin_island_contents),
                        res.getString(R.string.sumatra_contents),
                        res.getString(R.string.honshu_contents),
                        res.getString(R.string.great_britain_contents),
                        res.getString(R.string.ellesmere_island_contents),
                        res.getString(R.string.sulawesi_contents),
                        res.getString(R.string.south_island_contents),
                        res.getString(R.string.java_contents),
                        res.getString(R.string.north_island_contents),
                        res.getString(R.string.luzon_contents),
                        res.getString(R.string.newfoundland_island_contents),
                        res.getString(R.string.victoria_island_canada_contents),
                        res.getString(R.string.cuba_island_contents),
                        res.getString(R.string.iceland_island_contents),
                        res.getString(R.string.mindanao_contents),
                        res.getString(R.string.ireland_island_contents),
                        res.getString(R.string.hokkaido_contents),
                        res.getString(R.string.hispaniola_contents),
                        res.getString(R.string.sakhalin_contents),
                        res.getString(R.string.banks_island_contents),
                        res.getString(R.string.tasmania_contents),
                        res.getString(R.string.devon_island_contents),
                        res.getString(R.string.alexander_island_contents),
                        res.getString(R.string.isla_grande_de_tierra_del_fuego_contents),
                        res.getString(R.string.severny_island_contents),
                        res.getString(R.string.berkner_island_contents),
                        res.getString(R.string.axel_heiberg_island_contents),
                        res.getString(R.string.southampton_island_contents),
                        res.getString(R.string.marajó_contents),
                        res.getString(R.string.kyushu_contents),
                        res.getString(R.string.taiwan_island_contents),
                        res.getString(R.string.new_britain_contents),
                        res.getString(R.string.yuzhny_island_contents),
                        res.getString(R.string.hainan_contents),
                        res.getString(R.string.vancouver_island_contents),
                        res.getString(R.string.timor_contents),
                        res.getString(R.string.sicily)
                    }, cardLab);
            enterListInDatabase(new String[]{
                            res.getString(R.string.south_china_sea),
                            res.getString(R.string.caribbean_sea),
                            res.getString(R.string.mediterranean_sea),
                            res.getString(R.string.bering_sea),
                            res.getString(R.string.gulf_of_mexico),
                            res.getString(R.string.arabian_sea),
                            res.getString(R.string.sea_of_okhotsk),
                            res.getString(R.string.sea_of_japan),
                            res.getString(R.string.hudson_bay),
                            res.getString(R.string.east_china_sea),
                            res.getString(R.string.andaman_sea),
                            res.getString(R.string.black_sea),
                            res.getString(R.string.red_sea),
                            res.getString(R.string.black_sea),
                            res.getString(R.string.caspian_sea),
                            res.getString(R.string.bay_of_bengal),
                            res.getString(R.string.persian_gulf),
                            res.getString(R.string.gulf_of_oman),
                            res.getString(R.string.ionian_sea),
                            res.getString(R.string.aegean_sea),
                            res.getString(R.string.gulf_of_carpentaria),
                            res.getString(R.string.gulf_of_st_lawrence),
                            res.getString(R.string.gulf_of_saint_lawrence)
                    }, false, res.getString(R.string.major_world_seas).toLowerCase(),
                    new String[] {
                            res.getString(R.string.south_china_sea_contents),
                            res.getString(R.string.caribbean_sea_contents),
                            res.getString(R.string.mediterranean_sea_contents),
                            res.getString(R.string.bering_sea_contents),
                            res.getString(R.string.gulf_of_mexico_contents),
                            res.getString(R.string.arabian_sea_contents),
                            res.getString(R.string.sea_of_okhotsk_contents),
                            res.getString(R.string.sea_of_japan_contents),
                            res.getString(R.string.hudson_bay_contents),
                            res.getString(R.string.east_china_sea_contents),
                            res.getString(R.string.andaman_sea_contents),
                            res.getString(R.string.black_sea_contents),
                            res.getString(R.string.red_sea_contents),
                            res.getString(R.string.black_sea_contents),
                            res.getString(R.string.caspian_sea_contents),
                            res.getString(R.string.bay_of_bengal_contents),
                            res.getString(R.string.persian_gulf_contents),
                            res.getString(R.string.gulf_of_oman_contents),
                            res.getString(R.string.ionian_sea_contents),
                            res.getString(R.string.aegean_sea_contents),
                            res.getString(R.string.gulf_of_carpentaria_contents),
                            res.getString(R.string.gulf_of_st_lawrence_contents),
                            res.getString(R.string.gulf_of_saint_lawrence)
                    }, cardLab);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_information, container, false);

        mCardRecyclerView = (RecyclerView) view.findViewById(R.id.fragment_information_recycler_view);
        mCardRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mPopupTextView = (TextView) view.findViewById(R.id.fragment_information_delete_text_view);

        updateUI();

        getActivity().setTitle(GeoAnimator.capitalizeTitle(getArguments().getString("InformationCategoryFragment")));

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_subcategory_information_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.fragment_sub_information_action_search);
        this.searchItem = searchItem;
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                SubcategoryCardInfoLab cardLab = SubcategoryCardInfoLab.get(getActivity());
                List<SubcategoryCardInfoHolder> cardInfoHolderList = cardLab.getCategoryCardInfos(getArguments().getString("InformationCategoryFragment"));
                if (mQueryAdapter == null) {
                    mQueryAdapter = new CardAdapter(cardInfoHolderList);
                    mPopupTextView.setText(getResources().getString(R.string.no_search_results));
                    mCardRecyclerView.setAdapter(mQueryAdapter);
                } else {
                    mQueryAdapter.setCardInfoList(cardInfoHolderList);
                }

                for (int i = cardInfoHolderList.size() - 1; i >= 0; --i) {
                    String holderText = cardInfoHolderList.get(i).getTitle().toLowerCase();
                    holderText = holderText.replace(" ", "");
                    String newInstanceText = newText.toLowerCase();
                    newInstanceText = newInstanceText.replace(" ", "");
                    if (!holderText.contains(newInstanceText)) {
                        cardInfoHolderList.remove(i);
                    }
                }
                mQueryAdapter.notifyDataSetChanged();
                if (mQueryAdapter.getCardInfoList().size() == 0) {
                    mPopupTextView.setVisibility(View.VISIBLE);
                } else {
                    mPopupTextView.setVisibility(View.GONE);
                }
                return false;
            }
        });
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                mPopupTextView.setVisibility(View.GONE);
                mCardRecyclerView.setAdapter(mAdapter);
                return false;
            }
        });

        MenuItem plusItem = menu.findItem(R.id.fragment_sub_information_action_add);
        MenuItem deleteItem = menu.findItem(R.id.fragment_sub_information_action_delete);
        MenuItem updateItem = menu.findItem(R.id.fragment_sub_information_action_change);
        MenuItem goBackItem = menu.findItem(R.id.fragment_sub_information_action_back);
        if (isUndoDeleteCardMode) {
            searchItem.setVisible(false);
            deleteItem.setVisible(false);
            updateItem.setVisible(false);
            goBackItem.setVisible(false);
            plusItem.setIcon(R.drawable.ic_action_undo_delete);
            plusItem.setTitle(R.string.exit_delete_mode);
        } else if (isUpdateCardMode) {
            searchItem.setVisible(false);
            deleteItem.setVisible(false);
            updateItem.setVisible(false);
            goBackItem.setVisible(false);
            plusItem.setIcon(R.drawable.ic_action_undo_delete);
            plusItem.setTitle(R.string.exit_update_mode);
        } else {
            searchItem.setVisible(true);
            deleteItem.setVisible(true);
            updateItem.setVisible(true);
            goBackItem.setVisible(true);
            plusItem.setIcon(R.drawable.ic_action_add);
            plusItem.setTitle(R.string.add_a_subcategory);
            deleteItem.setTitle(R.string.delete_a_subcategory);
            updateItem.setTitle(R.string.update_a_subcategory);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.fragment_sub_information_action_add:
                if (isUndoDeleteCardMode || isUpdateCardMode) {
                    resetToNormalAdapter();
                } else {
                    FragmentManager manager = getActivity().getSupportFragmentManager();
                    SubInfoCardCreatorDialogFragment dialog = SubInfoCardCreatorDialogFragment.newInstance(
                            getString(R.string.title_colon), getString(R.string.title_name_subcat),
                            getArguments().getString("InformationCategoryFragment"));

                    dialog.setTargetFragment(InformationSubcategoryFragment.this, REQUEST_CREATE_CARD);
                    dialog.show(manager, DIALOG_CREATE_CARD);
                }
                return true;
            case R.id.fragment_sub_information_action_delete:
                isUndoDeleteCardMode = true;
                isUpdateCardMode = false;

                if (mDeleteAdapter == null) {
                    mDeleteAdapter = new CardAdapter(getOriginalsDeletedCardInfoList());
                } else {
                    mDeleteAdapter.setCardInfoList(getOriginalsDeletedCardInfoList());
                }

                mPopupTextView.setVisibility(View.VISIBLE);
                if (mDeleteAdapter.getCardInfoList().size() == 0) {
                    mPopupTextView.setText(getResources().getText(R.string.no_delete_blue_subcategories));
                } else {
                    mPopupTextView.setText(getResources().getText(R.string.delete_any_blue_subcategory));
                }

                mCardRecyclerView.swapAdapter(mDeleteAdapter, true);
                getActivity().invalidateOptionsMenu();

                GeoAnimator.makeDurationToast(getActivity(), getString(R.string.delete_mode), Gravity.CENTER, 0, 0, 1000);
                return true;
            case R.id.fragment_sub_information_action_change:
                isUpdateCardMode = true;
                isUndoDeleteCardMode = false;

                if (mUpdateAdapter == null) {
                    mUpdateAdapter = new CardAdapter(getOriginalsDeletedCardInfoList());
                } else {
                    mUpdateAdapter.setCardInfoList(getOriginalsDeletedCardInfoList());
                }

                mPopupTextView.setVisibility(View.VISIBLE);
                if (mUpdateAdapter.getCardInfoList().size() == 0) {
                    mPopupTextView.setText(getResources().getText(R.string.no_update_pressing_subcategory));
                } else {
                    mPopupTextView.setText(getResources().getText(R.string.update_pressing_subcategory));
                }

                mCardRecyclerView.swapAdapter(mUpdateAdapter, true);
                getActivity().invalidateOptionsMenu();

                GeoAnimator.makeDurationToast(getActivity(), getString(R.string.change_mode), Gravity.CENTER, 0, 0, 1000);
                return true;
            case R.id.fragment_sub_information_action_back:
                if (getActivity() instanceof MainMenuActivity) {
                    MainMenuActivity activity = (MainMenuActivity) getActivity();
                    InformationFragment fragment = InformationFragment.newInstance();
                    FragmentManager fm = getActivity().getSupportFragmentManager();
                    SearchView view = (SearchView) searchItem.getActionView();
                    view.setIconified(true);
                    activity.replaceFragmentInActivity(MainMenuActivity.FRAGMENT_CONTAINER, fragment,
                            fm, "Information");
                }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }

        if (requestCode == REQUEST_CREATE_CARD) {
            String cardTitle = (String) data.getSerializableExtra(SubInfoCardCreatorDialogFragment.EXTRA_SUB_TITLE);
            SubcategoryCardInfoHolder addCard = new SubcategoryCardInfoHolder(
                    cardTitle, true, getArguments().getString("InformationCategoryFragment"),
                    getString(R.string.write_copypaste_contents), getString(R.string.write_copypaste_notes));
            SubcategoryCardInfoLab cardLab = SubcategoryCardInfoLab.get(getActivity());
            cardLab.addCardInfo(addCard);
            mPopupTextView.setVisibility(View.GONE);
            updateUI();
            MainMenuActivity.increaseAdCount(getActivity());
        } else if (requestCode == REQUEST_CONFIRM_DELETE_CARD) {
            UUID cardDeleteUUID = (UUID) data.getSerializableExtra(ConfirmDeleteDialogFragment.EXTRA_ADDITIONAL);
            SubcategoryCardInfoLab cardInfoLab = SubcategoryCardInfoLab.get(getActivity());
            if (cardDeleteUUID != null) {
                cardInfoLab.deleteCardInfo(cardDeleteUUID);
                mAdapter.setCardInfoList(cardInfoLab.getCategoryCardInfos(getArguments().getString("InformationCategoryFragment")));
                mDeleteAdapter.setCardInfoList(getOriginalsDeletedCardInfoList());
                mDeleteAdapter.notifyDataSetChanged();
                if (mDeleteAdapter.getCardInfoList().size() == 0) {
                    mPopupTextView.setText(getResources().getText(R.string.no_delete_blue_subcategories));
                }
                MainMenuActivity.increaseAdCount(getActivity());
            }
        } else if (requestCode == REQUEST_UPDATE_CARD) {
            UUID cardUpdateUUID = (UUID) data.getSerializableExtra(SubUpdateCardDialogFragment.EXTRA_SUB_UUID);
            String cardTitle = (String) data.getSerializableExtra(SubUpdateCardDialogFragment.EXTRA_SUB_TITLE);
            String contents = (String) data.getSerializableExtra(SubUpdateCardDialogFragment.EXTRA_SUB_CONTENTS);
            String notes = (String) data.getSerializableExtra(SubUpdateCardDialogFragment.EXTRA_SUB_NOTES);
            SubcategoryCardInfoLab cardInfoLab = SubcategoryCardInfoLab.get(getActivity());
            if (cardUpdateUUID != null) {
                SubcategoryCardInfoHolder holder = new SubcategoryCardInfoHolder(cardUpdateUUID, cardTitle,
                        true, getArguments().getString("InformationCategoryFragment"), contents, notes);
                cardInfoLab.updateCardInfo(holder);
                mAdapter.setCardInfoList(cardInfoLab.getCategoryCardInfos(getArguments().getString("InformationCategoryFragment")));
                mUpdateAdapter.setCardInfoList(getOriginalsDeletedCardInfoList());
                mUpdateAdapter.notifyDataSetChanged();
                MainMenuActivity.increaseAdCount(getActivity());
            }
        }
    }

    private void updateUI() {
        SubcategoryCardInfoLab cardLab = SubcategoryCardInfoLab.get(getActivity());
        List<SubcategoryCardInfoHolder> cardInfoHolderList = cardLab.getCategoryCardInfos(getArguments().getString("InformationCategoryFragment"));
        if (mAdapter == null) {
            mAdapter = new CardAdapter(cardInfoHolderList);
            mCardRecyclerView.setAdapter(mAdapter);

            if (cardInfoHolderList.size() == 0) {
                mPopupTextView.setText(getResources().getString(R.string.no_subcategories));
                mPopupTextView.setVisibility(View.VISIBLE);
            }
        } else {
            mAdapter.setCardInfoList(cardInfoHolderList);
            mAdapter.notifyDataSetChanged();
        }
    }

    private class CardHolder extends RecyclerView.ViewHolder {

        private TextView mTitleTextView;
        private Button mAgreeButton;
        private Button mNotesButton;
        private LinearLayout mHolderRelativeLayout;

        public CardHolder(View itemView) {
            super(itemView);

            mTitleTextView = (TextView) itemView.findViewById(R.id.card_holder_title_text);
            mAgreeButton = (Button) itemView.findViewById(R.id.card_holder_read_more_button);
            mHolderRelativeLayout = (LinearLayout) itemView.findViewById(R.id.card_holder_linear_layout);
            mNotesButton = (Button) itemView.findViewById(R.id.card_holder_notes_button);
        }

        public void bindCardInfo(final SubcategoryCardInfoHolder cardInfo) {
            mTitleTextView.setText(cardInfo.getTitle());
            mAgreeButton.setText(getResources().getText(R.string.readmore));
            if (isUndoDeleteCardMode && cardInfo.getDeleteable()) {
                mAgreeButton.setVisibility(View.GONE);
                mNotesButton.setVisibility(View.GONE);
                mHolderRelativeLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        triggerDelete(cardInfo.getId(), cardInfo.getTitle());
                    }
                });
            } else if (isUpdateCardMode) {
                mAgreeButton.setVisibility(View.GONE);
                mNotesButton.setVisibility(View.GONE);
                mHolderRelativeLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        FragmentManager manager = getActivity().getSupportFragmentManager();
                        SubUpdateCardDialogFragment dialog = SubUpdateCardDialogFragment.newInstance(
                                getString(R.string.new_title_colon), cardInfo.getTitle(), cardInfo.getId(), cardInfo.getCategory(),
                                cardInfo.getContents(), cardInfo.getNotes());
                        dialog.setTargetFragment(InformationSubcategoryFragment.this, REQUEST_UPDATE_CARD);
                        dialog.show(manager, DIALOG_UPDATE_CARD);
                    }
                });
            } else  {
                mHolderRelativeLayout.setBackgroundColor(getResources().getColor(android.R.color.white));
                mAgreeButton.setEnabled(true);
                mAgreeButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = ContentPagerActivity.newIntent(getActivity(), cardInfo.getId(), false);
                        startActivity(intent);
                        mCallbacks.finishActivity();
                    }
                });
                mNotesButton.setEnabled(true);
                mNotesButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = ContentPagerActivity.newIntent(getActivity(), cardInfo.getId(), true);
                        startActivity(intent);
                        mCallbacks.finishActivity();
                    }
                });
                mHolderRelativeLayout.setOnClickListener(null);
            }
        }
    }

    private void triggerDelete(UUID uuid, String cardTitleString) {
        FragmentManager manager = getActivity().getSupportFragmentManager();
        ConfirmDeleteDialogFragment dialog = ConfirmDeleteDialogFragment.newInstance(
                getString(R.string.aresure_delete_category_question, cardTitleString),
                getString(R.string.deleting_subcategory_warning, cardTitleString),
                getString(R.string.delete), getString(R.string.dont_delete), uuid);

        dialog.setTargetFragment(InformationSubcategoryFragment.this, REQUEST_CONFIRM_DELETE_CARD);
        dialog.show(manager, DIALOG_CONFIRM_DELETE_CARD);
    }

    private class CardAdapter extends RecyclerView.Adapter<CardHolder> {

        private List<SubcategoryCardInfoHolder> mCardInfos;

        public CardAdapter(List<SubcategoryCardInfoHolder> cardInfoList) {
            mCardInfos = cardInfoList;
        }

        @Override
        public CardHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.small_card_holder_navigation, parent, false);
            return new CardHolder(view);
        }

        @Override
        public void onBindViewHolder(CardHolder holder, int position) {
            SubcategoryCardInfoHolder cardInfo = mCardInfos.get(position);
            holder.bindCardInfo(cardInfo);
        }

        @Override
        public int getItemCount() {
            return mCardInfos.size();
        }

        public void setCardInfoList(List<SubcategoryCardInfoHolder> newCardInformation) {
            mCardInfos = newCardInformation;
        }

        public List<SubcategoryCardInfoHolder> getCardInfoList() {
            return mCardInfos;
        }
    }

    private void resetToNormalAdapter() {
        SubcategoryCardInfoLab cardLab = SubcategoryCardInfoLab.get(getActivity());
        if (cardLab.getCategoryCardInfos(getArguments().getString("InformationCategoryFragment")).size() == 0) {
            mPopupTextView.setText(getResources().getString(R.string.no_subcategories));
            mPopupTextView.setVisibility(View.VISIBLE);
        } else {
            mPopupTextView.setVisibility(View.GONE);
        }
        Log.e(TAG, String.valueOf(mAdapter.getCardInfoList().size()));
        mCardRecyclerView.swapAdapter(mAdapter, true);
        isUndoDeleteCardMode = false;
        isUpdateCardMode = false;
        getActivity().invalidateOptionsMenu();
    }

    private List<SubcategoryCardInfoHolder> getOriginalsDeletedCardInfoList() {
        SubcategoryCardInfoLab cardLab = SubcategoryCardInfoLab.get(getActivity());
        List<SubcategoryCardInfoHolder> instance = cardLab.getCategoryCardInfos(getArguments().getString("InformationCategoryFragment"));
        boolean deletables[] = new boolean[instance.size()];
        int counter = 0;
        for (SubcategoryCardInfoHolder i : instance) {
            deletables[counter] = !i.getDeleteable();
            counter += 1;
        }

        for (int i = (instance.size() - 1); i >= 0; --i) {
            if (deletables[i]) {
                instance.remove(i);
            }
        }

        return instance;
    }

    private void enterListInDatabase(String[] itemList, boolean deletable, String categories, String[] contents,
                                     SubcategoryCardInfoLab lab) {
        for (int i = 0; i < itemList.length; ++i) {
            String item = itemList[i];
            SubcategoryCardInfoHolder instance = new SubcategoryCardInfoHolder(item, deletable, categories, contents[i], getString(R.string.write_copypaste_notes));
            lab.addCardInfo(instance);
        }
    }
    private void enterListInDatabase(String[] itemList, boolean deletable, String categories, int[] contents,
                                     SubcategoryCardInfoLab lab) {
        Resources res = getResources();
        for (int i = 0; i < itemList.length; ++i) {
            String item = itemList[i];
            SubcategoryCardInfoHolder instance = new SubcategoryCardInfoHolder(item, deletable, categories,
                    res.getString(contents[i]), getString(R.string.write_copypaste_notes));
            lab.addCardInfo(instance);
        }
    }

}

