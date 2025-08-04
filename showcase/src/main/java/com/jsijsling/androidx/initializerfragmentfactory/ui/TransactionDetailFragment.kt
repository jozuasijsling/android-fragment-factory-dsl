package com.jsijsling.androidx.initializerfragmentfactory.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.jsijsling.androidx.initializerfragmentfactory.data.Transaction
import com.jsijsling.androidx.initializerfragmentfactory.data.generateTransaction
import com.jsijsling.androidx.initializerfragmentfactory.databinding.ComposeFragmentBinding
import com.jsijsling.androidx.initializerfragmentfactory.ui.theme.SampleAppTheme

class TransactionDetailFragment() : Fragment() {

    private val transactionId get() = requireArguments().getInt(ARG_TRANSACTION_ID)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState != null) {
            Toast.makeText(requireContext(), "DetailFragment recreated", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return ComposeFragmentBinding.inflate(inflater, container, false).apply {
            val transaction = generateTransaction(transactionId)
            root.setContent {
                SampleAppTheme {
                    Scaffold { paddingValues ->
                        TransactionDetail(
                            transaction,
                            paddingValues,
                            onRecreateActivityClick = { requireActivity().recreate() },
                        )
                    }
                }
            }
        }.root
    }

    companion object {
        private const val ARG_TRANSACTION_ID = "transaction_id"
        fun args(transactionId: Int) = bundleOf(ARG_TRANSACTION_ID to transactionId)
    }
}

@Composable
fun TransactionDetail(
    transaction: Transaction,
    paddingValues: PaddingValues,
    onRecreateActivityClick: () -> Unit,
) {
    Column(
        Modifier
            .fillMaxWidth()
            .padding(paddingValues)
            .consumeWindowInsets(paddingValues)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Text(
            transaction.getFormattedValue(),
            Modifier.align(Alignment.CenterHorizontally),
            color = LocalContentColor.current,
            style = MaterialTheme.typography.displayMedium,
        )
        TransactionDetailSection("To:") {
            Text(
                transaction.beneficiaryName,
                style = MaterialTheme.typography.bodyLarge,
            )
            Text(transaction.beneficiaryIban)
        }
        TransactionDetailSection(label = "Description:") {
            Text(transaction.description)
        }
        Button(
            onClick = onRecreateActivityClick,
            Modifier.align(Alignment.CenterHorizontally),
        ) {
            Text("Recreate activity")
        }
    }
}

@Composable
private fun TransactionDetailSection(label: String, content: @Composable ColumnScope.() -> Unit) {
    Text(label, style = MaterialTheme.typography.labelMedium)
    Card {
        Column(
            Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            content()
        }
    }
}
