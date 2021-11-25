package com.goodswarehouse.scanner.data.track.bus

import com.goodswarehouse.scanner.data.util.RxBus
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FocusBus @Inject constructor() : RxBus<FocusEvent>()