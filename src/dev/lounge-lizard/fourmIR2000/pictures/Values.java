/*
 * Created on 20 sept. 2005
 */
package dev.lounge-lizard.fourmIR2000.pictures;

import dev.lounge-lizard.lawrence.impl.ImageFile;

/**
 * Contains the list of pictures used in our program
 */
public enum Values {
	// Frame
	@ImageFile("/resources/pictures/menu/main_icon.png")main_icon,
	
	// Game menu
	@ImageFile("/resources/pictures/menu/file_level_new.png")file_level_new,
	@ImageFile("/resources/pictures/menu/file_level_load.png")file_level_load,
	@ImageFile("/resources/pictures/menu/file_level_save.png")file_level_save,
	@ImageFile("/resources/pictures/menu/file_game_load.png")file_game_load,
	@ImageFile("/resources/pictures/menu/file_game_save.png")file_game_save,
	@ImageFile("/resources/pictures/menu/file_game_close.png")file_game_close,
	@ImageFile("/resources/pictures/menu/file_exit.png")file_exit,
	@ImageFile("/resources/pictures/menu/difficulty.png")difficulty,
	@ImageFile("/resources/pictures/menu/about.png")about,
		
	// Level editor
	@ImageFile("/resources/pictures/editor/edit_mountains.png")edit_mountain,
	@ImageFile("/resources/pictures/editor/edit_marsh.png")edit_marsh,
	@ImageFile("/resources/pictures/editor/edit_plain.png")edit_plain,
	@ImageFile("/resources/pictures/editor/edit_desert.png")edit_desert,
	@ImageFile("/resources/pictures/editor/edit_user.png")edit_user,
	@ImageFile("/resources/pictures/editor/edit_play.png")edit_play,
	@ImageFile("/resources/pictures/editor/elem_grass.png")edit_elem_grass,
	@ImageFile("/resources/pictures/editor/elem_sand.png")edit_elem_sand,
	@ImageFile("/resources/pictures/editor/elem_water.png")edit_elem_water,
	@ImageFile("/resources/pictures/editor/elem_food.png")edit_elem_food,
	@ImageFile("/resources/pictures/editor/elem_rock.png")edit_elem_rock,
	@ImageFile("/resources/pictures/editor/elem_hill.png")edit_elem_hill,
	@ImageFile("/resources/pictures/editor/elem_hill_opp.png")edit_elem_hill_opp,
	@ImageFile("/resources/pictures/editor/elem_remove.png")edit_elem_remove,
	@ImageFile("/resources/pictures/editor/elem_water_mini.png")edit_elem_water_mini,
	@ImageFile("/resources/pictures/editor/elem_food_mini.png")edit_elem_food_mini,
	@ImageFile("/resources/pictures/editor/elem_rock_mini.png")edit_elem_rock_mini,
	@ImageFile("/resources/pictures/editor/edit_load.png")edit_load,
	
	// Backgrounds
	@ImageFile("/resources/pictures/grounds/grass.png")grass,
	// Water
	@ImageFile("/resources/pictures/grounds/water/water.png")water,
	@ImageFile("/resources/pictures/grounds/water/w_b.png")water_B,
	@ImageFile("/resources/pictures/grounds/water/w_bl.png")water_BL,
	@ImageFile("/resources/pictures/grounds/water/w_blr.png")water_BLR,
	@ImageFile("/resources/pictures/grounds/water/w_br.png")water_BR,
	@ImageFile("/resources/pictures/grounds/water/w_l.png")water_L,
	@ImageFile("/resources/pictures/grounds/water/w_lr.png")water_LR,
	@ImageFile("/resources/pictures/grounds/water/w_r.png")water_R,
	@ImageFile("/resources/pictures/grounds/water/w_t.png")water_T,
	@ImageFile("/resources/pictures/grounds/water/w_tb.png")water_TB,
	@ImageFile("/resources/pictures/grounds/water/w_tbl.png")water_TBL,
	@ImageFile("/resources/pictures/grounds/water/w_tblr.png")water_TBLR,
	@ImageFile("/resources/pictures/grounds/water/w_tbr.png")water_TBR,
	@ImageFile("/resources/pictures/grounds/water/w_tl.png")water_TL,
	@ImageFile("/resources/pictures/grounds/water/w_tlr.png")water_TLR,
	@ImageFile("/resources/pictures/grounds/water/w_tr.png")water_TR,
	// Desert
	@ImageFile("/resources/pictures/grounds/desert/desert.png")desert,
	@ImageFile("/resources/pictures/grounds/desert/d_b.png")desert_B,
	@ImageFile("/resources/pictures/grounds/desert/d_bl.png")desert_BL,
	@ImageFile("/resources/pictures/grounds/desert/d_blr.png")desert_BLR,
	@ImageFile("/resources/pictures/grounds/desert/d_br.png")desert_BR,
	@ImageFile("/resources/pictures/grounds/desert/d_l.png")desert_L,
	@ImageFile("/resources/pictures/grounds/desert/d_lr.png")desert_LR,
	@ImageFile("/resources/pictures/grounds/desert/d_r.png")desert_R,
	@ImageFile("/resources/pictures/grounds/desert/d_t.png")desert_T,
	@ImageFile("/resources/pictures/grounds/desert/d_tb.png")desert_TB,
	@ImageFile("/resources/pictures/grounds/desert/d_tbl.png")desert_TBL,
	@ImageFile("/resources/pictures/grounds/desert/d_tblr.png")desert_TBLR,
	@ImageFile("/resources/pictures/grounds/desert/d_tbr.png")desert_TBR,
	@ImageFile("/resources/pictures/grounds/desert/d_tl.png")desert_TL,
	@ImageFile("/resources/pictures/grounds/desert/d_tlr.png")desert_TLR,
	@ImageFile("/resources/pictures/grounds/desert/d_tr.png")desert_TR,

	// Game elements
	@ImageFile("/resources/pictures/game/food.png")food,
	@ImageFile("/resources/pictures/game/rock.png")rock,
	@ImageFile("/resources/pictures/game/anthill.png")antHill,
	@ImageFile("/resources/pictures/game/anthill_opp.png")antHill_enemy,
	
	// Insects
	// Yellow : ant soldier
	@ImageFile("/resources/pictures/insects/yellow/s_l.png")	antYellow_SL,
	@ImageFile("/resources/pictures/insects/yellow/s_ul.png")antYellow_SUL,
	@ImageFile("/resources/pictures/insects/yellow/s_u.png")	antYellow_SU,
	@ImageFile("/resources/pictures/insects/yellow/s_ur.png")antYellow_SUR,
	@ImageFile("/resources/pictures/insects/yellow/s_r.png")	antYellow_SR,
	@ImageFile("/resources/pictures/insects/yellow/s_dr.png")antYellow_SDR,
	@ImageFile("/resources/pictures/insects/yellow/s_d.png")	antYellow_SD,
	@ImageFile("/resources/pictures/insects/yellow/s_dl.png")antYellow_SDL,
	// Yellow : ant worker
	@ImageFile("/resources/pictures/insects/yellow/w_l.png")	antYellow_WL,
	@ImageFile("/resources/pictures/insects/yellow/w_ul.png")antYellow_WUL,
	@ImageFile("/resources/pictures/insects/yellow/w_u.png")	antYellow_WU,
	@ImageFile("/resources/pictures/insects/yellow/w_ur.png")antYellow_WUR,
	@ImageFile("/resources/pictures/insects/yellow/w_r.png")	antYellow_WR,
	@ImageFile("/resources/pictures/insects/yellow/w_dr.png")antYellow_WDR,
	@ImageFile("/resources/pictures/insects/yellow/w_d.png")	antYellow_WD,
	@ImageFile("/resources/pictures/insects/yellow/w_dl.png")antYellow_WDL,
	// Yellow : ant Kamikaze
	@ImageFile("/resources/pictures/insects/yellow/k_l.png")		antYellow_KL,
	@ImageFile("/resources/pictures/insects/yellow/k_ul.png")	antYellow_KUL,
	@ImageFile("/resources/pictures/insects/yellow/k_u.png")		antYellow_KU,
	@ImageFile("/resources/pictures/insects/yellow/k_ur.png")	antYellow_KUR,
	@ImageFile("/resources/pictures/insects/yellow/k_r.png")		antYellow_KR,
	@ImageFile("/resources/pictures/insects/yellow/k_dr.png")	antYellow_KDR,
	@ImageFile("/resources/pictures/insects/yellow/k_d.png")		antYellow_KD,
	@ImageFile("/resources/pictures/insects/yellow/k_dl.png")	antYellow_KDL,
	// Yellow : bug
    @ImageFile("/resources/pictures/insects/yellow/b_l.png") antYellow_BL,
    @ImageFile("/resources/pictures/insects/yellow/b_ul.png")antYellow_BUL,
    @ImageFile("/resources/pictures/insects/yellow/b_u.png") antYellow_BU,
    @ImageFile("/resources/pictures/insects/yellow/b_ur.png")antYellow_BUR,
    @ImageFile("/resources/pictures/insects/yellow/b_r.png") antYellow_BR,
    @ImageFile("/resources/pictures/insects/yellow/b_dr.png")antYellow_BDR,
    @ImageFile("/resources/pictures/insects/yellow/b_d.png") antYellow_BD,
    @ImageFile("/resources/pictures/insects/yellow/b_dl.png")antYellow_BDL,
	// Black : ant soldier
	@ImageFile("/resources/pictures/insects/black/s_l.png")	antBlack_SL,
	@ImageFile("/resources/pictures/insects/black/s_ul.png")	antBlack_SUL,
	@ImageFile("/resources/pictures/insects/black/s_u.png")	antBlack_SU,
	@ImageFile("/resources/pictures/insects/black/s_ur.png")	antBlack_SUR,
	@ImageFile("/resources/pictures/insects/black/s_r.png")	antBlack_SR,
	@ImageFile("/resources/pictures/insects/black/s_dr.png")	antBlack_SDR,
	@ImageFile("/resources/pictures/insects/black/s_d.png")	antBlack_SD,
	@ImageFile("/resources/pictures/insects/black/s_dl.png")	antBlack_SDL,
	// Black : ant worker
	@ImageFile("/resources/pictures/insects/black/w_l.png")	antBlack_WL,
	@ImageFile("/resources/pictures/insects/black/w_ul.png")	antBlack_WUL,
	@ImageFile("/resources/pictures/insects/black/w_u.png")	antBlack_WU,
	@ImageFile("/resources/pictures/insects/black/w_ur.png")	antBlack_WUR,
	@ImageFile("/resources/pictures/insects/black/w_r.png")	antBlack_WR,
	@ImageFile("/resources/pictures/insects/black/w_dr.png")	antBlack_WDR,
	@ImageFile("/resources/pictures/insects/black/w_d.png")	antBlack_WD,
	@ImageFile("/resources/pictures/insects/black/w_dl.png")	antBlack_WDL,
	// Black : ant Kamikaze
	@ImageFile("/resources/pictures/insects/black/k_l.png")	antBlack_KL,
	@ImageFile("/resources/pictures/insects/black/k_ul.png")	antBlack_KUL,
	@ImageFile("/resources/pictures/insects/black/k_u.png")	antBlack_KU,
	@ImageFile("/resources/pictures/insects/black/k_ur.png")	antBlack_KUR,
	@ImageFile("/resources/pictures/insects/black/k_r.png")	antBlack_KR,
	@ImageFile("/resources/pictures/insects/black/k_dr.png")	antBlack_KDR,
	@ImageFile("/resources/pictures/insects/black/k_d.png")	antBlack_KD,
	@ImageFile("/resources/pictures/insects/black/k_dl.png")	antBlack_KDL,
	// Black : bug
    @ImageFile("/resources/pictures/insects/black/b_l.png") antBlack_BL,
    @ImageFile("/resources/pictures/insects/black/b_ul.png")antBlack_BUL,
    @ImageFile("/resources/pictures/insects/black/b_u.png") antBlack_BU,
    @ImageFile("/resources/pictures/insects/black/b_ur.png")antBlack_BUR,
    @ImageFile("/resources/pictures/insects/black/b_r.png") antBlack_BR,
    @ImageFile("/resources/pictures/insects/black/b_dr.png")antBlack_BDR,
    @ImageFile("/resources/pictures/insects/black/b_d.png") antBlack_BD,
    @ImageFile("/resources/pictures/insects/black/b_dl.png")antBlack_BDL,
	// Red : ant soldier
	@ImageFile("/resources/pictures/insects/red/s_l.png")	antRed_SL,
	@ImageFile("/resources/pictures/insects/red/s_ul.png")	antRed_SUL,
	@ImageFile("/resources/pictures/insects/red/s_u.png")	antRed_SU,
	@ImageFile("/resources/pictures/insects/red/s_ur.png")	antRed_SUR,
	@ImageFile("/resources/pictures/insects/red/s_r.png")	antRed_SR,
	@ImageFile("/resources/pictures/insects/red/s_dr.png")	antRed_SDR,
	@ImageFile("/resources/pictures/insects/red/s_d.png")	antRed_SD,
	@ImageFile("/resources/pictures/insects/red/s_dl.png")	antRed_SDL,
	// Red : ant worker
	@ImageFile("/resources/pictures/insects/red/w_l.png")	antRed_WL,
	@ImageFile("/resources/pictures/insects/red/w_ul.png")	antRed_WUL,
	@ImageFile("/resources/pictures/insects/red/w_u.png")	antRed_WU,
	@ImageFile("/resources/pictures/insects/red/w_ur.png")	antRed_WUR,
	@ImageFile("/resources/pictures/insects/red/w_r.png")	antRed_WR,
	@ImageFile("/resources/pictures/insects/red/w_dr.png")	antRed_WDR,
	@ImageFile("/resources/pictures/insects/red/w_d.png")	antRed_WD,
	@ImageFile("/resources/pictures/insects/red/w_dl.png")	antRed_WDL,
	// Red : ant Kamikaze
	@ImageFile("/resources/pictures/insects/red/k_l.png")	antRed_KL,
	@ImageFile("/resources/pictures/insects/red/k_ul.png")	antRed_KUL,
	@ImageFile("/resources/pictures/insects/red/k_u.png")	antRed_KU,
	@ImageFile("/resources/pictures/insects/red/k_ur.png")	antRed_KUR,
	@ImageFile("/resources/pictures/insects/red/k_r.png")	antRed_KR,
	@ImageFile("/resources/pictures/insects/red/k_dr.png")	antRed_KDR,
	@ImageFile("/resources/pictures/insects/red/k_d.png")	antRed_KD,
	@ImageFile("/resources/pictures/insects/red/k_dl.png")	antRed_KDL,
    // Red : bug
    @ImageFile("/resources/pictures/insects/red/b_l.png") antRed_BL,
    @ImageFile("/resources/pictures/insects/red/b_ul.png")antRed_BUL,
    @ImageFile("/resources/pictures/insects/red/b_u.png") antRed_BU,
    @ImageFile("/resources/pictures/insects/red/b_ur.png")antRed_BUR,
    @ImageFile("/resources/pictures/insects/red/b_r.png") antRed_BR,
    @ImageFile("/resources/pictures/insects/red/b_dr.png")antRed_BDR,
    @ImageFile("/resources/pictures/insects/red/b_d.png") antRed_BD,
    @ImageFile("/resources/pictures/insects/red/b_dl.png")antRed_BDL,
	// Brown : ant soldier
	@ImageFile("/resources/pictures/insects/brown/s_l.png")	antBrown_SL,
	@ImageFile("/resources/pictures/insects/brown/s_ul.png")	antBrown_SUL,
	@ImageFile("/resources/pictures/insects/brown/s_u.png")	antBrown_SU,
	@ImageFile("/resources/pictures/insects/brown/s_ur.png")	antBrown_SUR,
	@ImageFile("/resources/pictures/insects/brown/s_r.png")	antBrown_SR,
	@ImageFile("/resources/pictures/insects/brown/s_dr.png")	antBrown_SDR,
	@ImageFile("/resources/pictures/insects/brown/s_d.png")	antBrown_SD,
	@ImageFile("/resources/pictures/insects/brown/s_dl.png")	antBrown_SDL,
	// Brown : ant worker
	@ImageFile("/resources/pictures/insects/brown/w_l.png")	antBrown_WL,
	@ImageFile("/resources/pictures/insects/brown/w_ul.png")	antBrown_WUL,
	@ImageFile("/resources/pictures/insects/brown/w_u.png")	antBrown_WU,
	@ImageFile("/resources/pictures/insects/brown/w_ur.png")	antBrown_WUR,
	@ImageFile("/resources/pictures/insects/brown/w_r.png")	antBrown_WR,
	@ImageFile("/resources/pictures/insects/brown/w_dr.png")	antBrown_WDR,
	@ImageFile("/resources/pictures/insects/brown/w_d.png")	antBrown_WD,
	@ImageFile("/resources/pictures/insects/brown/w_dl.png")	antBrown_WDL,
	// Brown : ant Kamikaze
	@ImageFile("/resources/pictures/insects/brown/k_l.png")	antBrown_KL,
	@ImageFile("/resources/pictures/insects/brown/k_ul.png")	antBrown_KUL,
	@ImageFile("/resources/pictures/insects/brown/k_u.png")	antBrown_KU,
	@ImageFile("/resources/pictures/insects/brown/k_ur.png")	antBrown_KUR,
	@ImageFile("/resources/pictures/insects/brown/k_r.png")	antBrown_KR,
	@ImageFile("/resources/pictures/insects/brown/k_dr.png")	antBrown_KDR,
	@ImageFile("/resources/pictures/insects/brown/k_d.png")	antBrown_KD,
	@ImageFile("/resources/pictures/insects/brown/k_dl.png")	antBrown_KDL,
    // Brown : bug
    @ImageFile("/resources/pictures/insects/brown/b_l.png") antBrown_BL,
    @ImageFile("/resources/pictures/insects/brown/b_ul.png")antBrown_BUL,
    @ImageFile("/resources/pictures/insects/brown/b_u.png") antBrown_BU,
    @ImageFile("/resources/pictures/insects/brown/b_ur.png")antBrown_BUR,
    @ImageFile("/resources/pictures/insects/brown/b_r.png") antBrown_BR,
    @ImageFile("/resources/pictures/insects/brown/b_dr.png")antBrown_BDR,
    @ImageFile("/resources/pictures/insects/brown/b_d.png") antBrown_BD,
    @ImageFile("/resources/pictures/insects/brown/b_dl.png")antBrown_BDL,
	// White : ant soldier
	@ImageFile("/resources/pictures/insects/white/s_l.png")	antWhite_SL,
	@ImageFile("/resources/pictures/insects/white/s_ul.png")	antWhite_SUL,
	@ImageFile("/resources/pictures/insects/white/s_u.png")	antWhite_SU,
	@ImageFile("/resources/pictures/insects/white/s_ur.png")	antWhite_SUR,
	@ImageFile("/resources/pictures/insects/white/s_r.png")	antWhite_SR,
	@ImageFile("/resources/pictures/insects/white/s_dr.png")	antWhite_SDR,
	@ImageFile("/resources/pictures/insects/white/s_d.png")	antWhite_SD,
	@ImageFile("/resources/pictures/insects/white/s_dl.png")	antWhite_SDL,
	// White : ant worker
	@ImageFile("/resources/pictures/insects/white/w_l.png")	antWhite_WL,
	@ImageFile("/resources/pictures/insects/white/w_ul.png")	antWhite_WUL,
	@ImageFile("/resources/pictures/insects/white/w_u.png")	antWhite_WU,
	@ImageFile("/resources/pictures/insects/white/w_ur.png")	antWhite_WUR,
	@ImageFile("/resources/pictures/insects/white/w_r.png")	antWhite_WR,
	@ImageFile("/resources/pictures/insects/white/w_dr.png")	antWhite_WDR,
	@ImageFile("/resources/pictures/insects/white/w_d.png")	antWhite_WD,
	@ImageFile("/resources/pictures/insects/white/w_dl.png")	antWhite_WDL,
	// White : ant Kamikaze
	@ImageFile("/resources/pictures/insects/white/k_l.png")	antWhite_KL,
	@ImageFile("/resources/pictures/insects/white/k_ul.png")	antWhite_KUL,
	@ImageFile("/resources/pictures/insects/white/k_u.png")	antWhite_KU,
	@ImageFile("/resources/pictures/insects/white/k_ur.png")	antWhite_KUR,
	@ImageFile("/resources/pictures/insects/white/k_r.png")	antWhite_KR,
	@ImageFile("/resources/pictures/insects/white/k_dr.png")	antWhite_KDR,
	@ImageFile("/resources/pictures/insects/white/k_d.png")	antWhite_KD,
	@ImageFile("/resources/pictures/insects/white/k_dl.png")	antWhite_KDL,
    // White : bug
    @ImageFile("/resources/pictures/insects/white/b_l.png") antWhite_BL,
    @ImageFile("/resources/pictures/insects/white/b_ul.png")antWhite_BUL,
    @ImageFile("/resources/pictures/insects/white/b_u.png") antWhite_BU,
    @ImageFile("/resources/pictures/insects/white/b_ur.png")antWhite_BUR,
    @ImageFile("/resources/pictures/insects/white/b_r.png") antWhite_BR,
    @ImageFile("/resources/pictures/insects/white/b_dr.png")antWhite_BDR,
    @ImageFile("/resources/pictures/insects/white/b_d.png") antWhite_BD,
    @ImageFile("/resources/pictures/insects/white/b_dl.png")antWhite_BDL,
	
	// Actions
    @ImageFile("/resources/pictures/game/ins_fight.png")		ins_fight,
    @ImageFile("/resources/pictures/game/ins_food.png")		ins_food,
    @ImageFile("/resources/pictures/game/ins_help.png")		ins_help,
    @ImageFile("/resources/pictures/game/ins_poison.png")	ins_poison,
    @ImageFile("/resources/pictures/game/select.png")		select,
    
    // Ranks
    @ImageFile("/resources/pictures/game/rnk_sergeant.png")	rnk_sergeant,
    @ImageFile("/resources/pictures/game/rnk_captain.png")	rnk_captain,
    @ImageFile("/resources/pictures/game/rnk_major.png")	rnk_major,
    @ImageFile("/resources/pictures/game/rnk_colonel.png")	rnk_colonel,
    @ImageFile("/resources/pictures/game/rnk_general.png")	rnk_general,
    
    // Game menus
    @ImageFile("/resources/pictures/game/buy_bug.png")buyBug,
    @ImageFile("/resources/pictures/game/buy_shield.png")buyShield,
    @ImageFile("/resources/pictures/game/buy_kamikaze.png")buyKamikaze,
    @ImageFile("/resources/pictures/game/buy_veteran.png")buyVeteran,
    @ImageFile("/resources/pictures/game/buy_bug_gray.png")buyBugGray,
    @ImageFile("/resources/pictures/game/buy_shield_gray.png")buyShieldGray,
    @ImageFile("/resources/pictures/game/buy_kamikaze_gray.png")buyKamikazeGray,
    @ImageFile("/resources/pictures/game/buy_veteran_gray.png")buyVeteranGray
    ;

	
	/**
	 * Indicate if a ground element is an obstacle or not
	 * @param value	the element to test
	 * @return true if it is an obstacle
	 */
	public static boolean isObstacle(final Values value) {
		return value == Values.rock
			? true
			: false;
	}
	
	
	/**
	 * Return the difficulty it takes to walk on a certain kind of tile
	 * @param value the value to test.
	 * @return the difficulty
	 */
	public static int getDifficultyCrossing(final Values value) {
		if (value == Values.desert)
			return 2;
		if (value == Values.water)
			return 4;
		return 1;
	}
	
	
}
