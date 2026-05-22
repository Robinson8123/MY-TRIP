import 'package:flutter_test/flutter_test.dart';
import 'package:midestino_movil/main.dart';

void main() {
  testWidgets('App arranca sin errores', (WidgetTester tester) async {
    await tester.pumpWidget(const MiDestinoApp());
    expect(find.byType(MiDestinoApp), findsOneWidget);
  });
}
