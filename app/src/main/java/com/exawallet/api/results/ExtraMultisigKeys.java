package com.exawallet.api.results;

public class ExtraMultisigKeys {
    ExtraMultisigKey[] extra_multisig_infos;

    public String[] getKeys() {
        String[] result = new String[extra_multisig_infos.length];

        for (int index = 0; index < extra_multisig_infos.length; index++) {
            result[index] = extra_multisig_infos[index].extra_multisig_info;
        }

        return result;
    }
}