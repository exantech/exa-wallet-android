package com.exawallet.api.results;

public class MultisigInfos {
    MultisigInfo[] multisig_infos;

    public String[] getMultisigInfos() {
        String[] result = new String[multisig_infos.length];

        for (int index = 0; index < multisig_infos.length; index++) {
            result[index] = multisig_infos[index].multisig_info;
        }

        return result;
    }
}