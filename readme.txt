the user can browse the menu by applying filters, searching by categories and sorting by price
to pay the order, the user has to use his shop card 
details of card are hardcoded for convenience but can be changed. Details are:
card number: 000
initial amount: 10,000

the user can add item to cart by browsing through the menu.
after that he can checkout by providing his card and confirming the payment.

once payment is done, the order will be generated with a random order id. The worder will be listed under the pending order.

then by logging as admin we can view pending orders.
under view pending orders, there is a option that process all pending orders, giving VIP orders top priority.
after processing the pending orders, they will be marked as OUT_OF_DELIVERY.

after few time, the OUT_OF_DELIVERY orders will be marked as completed which simulates the real life scenario.

the admin can add, delete, update items.



default costumers:
email = a, password = a (NORMAL)
email = b, password = b (NORMAL)
email = c, password = c (VIP)
email = d, password = d (VIP)

default admin:
email = ad, password = ad



I have used Swing instead of JavaFX.

i have added to view the GUI for Customer and Admin.

I am also using serialisation using inputStream and outputStream to save to object to a file using\
binary representation and then reading it back.