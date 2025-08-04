package com.jsijsling.androidx.initializerfragmentfactory.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import com.jsijsling.androidx.initializerfragmentfactory.data.Transaction
import com.jsijsling.androidx.initializerfragmentfactory.data.generateTransaction
import com.jsijsling.androidx.initializerfragmentfactory.databinding.ComposeFragmentBinding
import com.jsijsling.androidx.initializerfragmentfactory.ui.theme.SampleAppTheme

class TransactionListFragment(
    private val onItemSelected: (Transaction) -> Unit,
) : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        return ComposeFragmentBinding.inflate(inflater, container, false).apply {
            val items = List(100, ::generateTransaction)
            root.setContent {
                SampleAppTheme {
                    Scaffold { paddingValues ->
                        TransactionList(items, paddingValues, onItemSelected)
                    }
                }
            }
        }.root
    }
}

@Composable
private fun TransactionList(
    transactions: List<Transaction>,
    paddingValues: PaddingValues,
    onItemSelected: (Transaction) -> Unit,
) {
    LazyColumn(Modifier.consumeWindowInsets(paddingValues), contentPadding = paddingValues) {
        items(transactions) { transaction ->
            Surface(tonalElevation = 2.dp, shadowElevation = 2.dp) {
                TransactionListItem(transaction, onItemSelected, Modifier.fillParentMaxWidth())
            }
        }
    }
}

@Composable
fun TransactionListItem(
    transaction: Transaction,
    onItemSelected: (Transaction) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier
            .clickable(true) { onItemSelected(transaction) }
            .padding(16.dp),
    ) {
        Text(
            transaction.beneficiaryName,
            Modifier.weight(1F, fill = true),
        )
        Text(
            transaction.getFormattedValue(),
            color = LocalContentColor.current,
            style = MaterialTheme.typography.bodyLarge,
        )
    }
}

@Preview
@Composable
private fun TransactionListPreview() {
    SampleAppTheme {
        TransactionList(
            transactions = listOf(
                generateTransaction(1),
                generateTransaction(2),
                generateTransaction(3),
            ),
            paddingValues = PaddingValues(0.dp),
            onItemSelected = {},
        )
    }
}
