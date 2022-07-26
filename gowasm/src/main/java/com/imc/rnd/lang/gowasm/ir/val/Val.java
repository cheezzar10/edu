package com.imc.rnd.lang.gowasm.ir.val;

import java.util.Optional;

public interface Val {
    Optional<String> getName();

    ValKind getKind();
}
