<?php 


require 'vendor/autoload.php';
//if(isset($_POST['authKey']) && ($_POST['authKey']=="abc")){
if(1){
$stripe = new \Stripe\StripeClient('sk_test_51NPiF0GrVdHHP7ZZ4SDTdo30CpCl7UGCNmp7IPGDyjhjUuWtySQfDAcq6w2yjJdVOARBjRD3doO1dQ18oGLcrfAL00r2DbIty4');

// Use an existing Customer ID if this is a returning customer.
$customer = $stripe->customers->create(
[
    'name' => "Vishnu",
    'address' => [
        'line1' => 'Demo address',
        'postal_code' => '456456',
        'city' => 'New york',
        'state' => 'NY',
        'country' => 'US'
    ]
]
);
$ephemeralKey = $stripe->ephemeralKeys->create([
  'customer' => $customer->id,
], [
  'stripe_version' => '2022-08-01',
]);
$paymentIntent = $stripe->paymentIntents->create([
  'amount' => 1000,
  'currency' => 'usd',
  'description' => 'Resim içerikli pdf ödeme',
  'customer' => $customer->id,
  'automatic_payment_methods' => [
    'enabled' => 'true',
  ],
]);

echo json_encode(
  [
    'paymentIntent' => $paymentIntent->client_secret,
    'ephemeralKey' => $ephemeralKey->secret,
    'customer' => $customer->id,
    'publishableKey' => 'pk_test_51NPiF0GrVdHHP7ZZUJdltxZrm8gsrv2TGfPSHaAmJsuSIsNRui1ORsPy5WUIwTPtOpg90geiQLRZN0GgGHFQx4HN00GwmhKsE9'
  ]
);
http_response_code(200);
}   echo "prob var";


?>