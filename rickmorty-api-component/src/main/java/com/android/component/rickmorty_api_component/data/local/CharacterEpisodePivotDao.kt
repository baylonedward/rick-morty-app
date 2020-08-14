package com.android.component.rickmorty_api_component.data.local

import androidx.room.Dao
import com.android.component.rickmorty_api_component.data.entities.pivot.CharacterEpisodePivot

/**
 * Created by: ebaylon.
 * Created on: 14/08/2020.
 */
@Dao
interface CharacterEpisodePivotDao : BaseDao<CharacterEpisodePivot>